package com.event.booking.userservice.service;

import com.event.booking.userservice.dto.LoginRequest;
import com.event.booking.userservice.dto.RegisterRequest;
import com.event.booking.userservice.exception.InvalidCredentials;
import com.event.booking.userservice.exception.UserAlreadyExistsException;
import com.event.booking.userservice.exception.UserNotFoundException;
import com.event.booking.userservice.model.User;
import com.event.booking.userservice.model.enums.AuthProvider;
import com.event.booking.userservice.model.enums.Role;
import com.event.booking.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setup(){
        registerRequest = RegisterRequest.builder()
                .name("Test User")
                .password("password")
                .email("test@gmail.com")
                .role("USER")
                .build();

        user = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .password("hashedPassword")
                .email("test@gmail.com")
                .role(Role.USER)
                .authProvider(AuthProvider.LOCAL)
                .build();

        loginRequest = LoginRequest.builder()
                .email("test@gmail.com")
                .password("password")
                .build();
    }

    @Test
    void shouldRegisterUserSuccessfullyTest() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user, false)).thenReturn("accessToken");
        when(jwtService.generateToken(user, true)).thenReturn("refreshToken");

        // When
        Map<String, String> tokens = userService.register(registerRequest);

        // Then
        assertEquals("accessToken", tokens.get("access_token"));
        assertEquals("refreshToken", tokens.get("refresh_token"));

        verify(userRepository).existsByEmail(registerRequest.getEmail());
        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(user, false);
        verify(jwtService).generateToken(user, true);
    }

    @Test
    void shouldThrowExceptionIfEmailExistsTest() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(registerRequest));

        verify(userRepository).existsByEmail(registerRequest.getEmail());
        verifyNoMoreInteractions(userRepository, passwordEncoder, jwtService);
    }

    @Test
    void shouldLoginUserSuccessfullyTest() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken(user, false)).thenReturn("accessToken");
        when(jwtService.generateToken(user, true)).thenReturn("refreshToken");

        // When
        Map<String, String> tokens = userService.login(loginRequest);

        // Then
        assertEquals("accessToken", tokens.get("access_token"));
        assertEquals("refreshToken", tokens.get("refresh_token"));

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), "hashedPassword");
        verify(jwtService).generateToken(user, false);
        verify(jwtService).generateToken(user, true);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundTest() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.login(loginRequest));

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verifyNoMoreInteractions(passwordEncoder, jwtService);
    }

    @Test
    void shouldThrowExceptionForInvalidPasswordTest() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), "hashedPassword")).thenReturn(false);

        assertThrows(InvalidCredentials.class, () -> userService.login(loginRequest));

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), "hashedPassword");
        verifyNoMoreInteractions(jwtService);
    }
}