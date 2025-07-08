package com.event.booking.userservice.service;

import com.event.booking.userservice.dto.AuthResponse;
import com.event.booking.userservice.dto.LoginRequest;
import com.event.booking.userservice.dto.RegisterRequest;
import com.event.booking.userservice.dto.UserResponse;
import com.event.booking.userservice.exception.InvalidCredentials;
import com.event.booking.userservice.exception.UserAlreadyExistsException;
import com.event.booking.userservice.exception.UserNotFoundException;
import com.event.booking.userservice.mapper.UserMapper;
import com.event.booking.userservice.model.User;
import com.event.booking.userservice.model.enums.Role;
import com.event.booking.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.event.booking.userservice.constant.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail();
        if (existsByEmail(email)){
            log.error(USER_ALREADY_EXISTS_WITH_EMAIL + COLON + CURLY_PLACEHOLDER, email);
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_WITH_EMAIL + COLON + email);
        }

        User user = UserMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);

        String token = jwtService.generateToken(user);

        log.info("New user registered with email: {}", user.getEmail());

        return UserMapper.toAuthResponse(user, token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_WITH_EMAIL + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new InvalidCredentials(INVALID_CREDENTIALS);
        }

        String token = jwtService.generateToken(user);

        log.info("Login successful for user: {}", user.getEmail());

        return UserMapper.toAuthResponse(user, token);
    }

    @Override
    public UserResponse processOAuthLogin(String email, String name) {
        return null;
    }

    @Override
    public UserResponse getProfile() {
        return null;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return List.of();
    }

    @Override
    public void updateRole(String id, Role newRole) {

    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
