package com.event.booking.userservice.controller;

import com.event.booking.userservice.config.SecurityConfig;
import com.event.booking.userservice.dto.LoginRequest;
import com.event.booking.userservice.dto.RegisterRequest;
import com.event.booking.userservice.exception.CustomAccessDeniedHandler;
import com.event.booking.userservice.service.JwtService;
import com.event.booking.userservice.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Test
    void shouldRegisterUserSuccessfullyTest() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("Test User")
                .password("password")
                .email("test@gmail.com")
                .role("USER")
                .build();

        Map<String, String> tokens = Map.of(
                "access_token", "accessToken",
                "refresh_token", "refreshToken"
        );

        when(userService.register(any(RegisterRequest.class))).thenReturn(tokens);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.access_token").value("accessToken"));
    }

    @Test
    void shouldReturnBadRequestForInvalidRegisterRequestTest() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .email("")
                .build();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldLoginUserSuccessfullyTest() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("test@gmail.com")
                .password("password")
                .build();

        Map<String, String> tokens = Map.of(
                "access_token", "accessToken",
                "refresh_token", "refreshToken"
        );

        when(userService.login(any(LoginRequest.class))).thenReturn(tokens);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("accessToken"));
    }

    @Test
    void shouldReturnBadRequestForInvalidLoginRequestTest() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRefreshAccessTokenTest() throws Exception {
        when(userService.refresh("validRefreshToken")).thenReturn("newAccessToken");

        mockMvc.perform(post("/api/auth/refresh")
                .cookie(new Cookie("refresh_token", "validRefreshToken")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("newAccessToken"));
    }

    @Test
    void shouldReturnBadRequestWhenRefreshTokenMissingTest() throws Exception{
        mockMvc.perform(post("/api/auth/refresh"))
                .andExpect(status().isBadRequest());
    }
}