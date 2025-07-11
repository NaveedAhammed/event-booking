package com.event.booking.userservice.service;

import com.event.booking.userservice.dto.AuthResponse;
import com.event.booking.userservice.dto.LoginRequest;
import com.event.booking.userservice.dto.RegisterRequest;
import com.event.booking.userservice.dto.UserResponse;
import com.event.booking.userservice.model.User;
import com.event.booking.userservice.model.enums.Role;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, String> register(RegisterRequest request);

    Map<String, String> login(LoginRequest request);

    Map<String, String> oAuth(String email, String name);

    String refresh(String refreshToken);

    UserResponse processOAuthLogin(String email, String name);

    UserResponse getProfile();

    List<UserResponse> getAllUsers();

    void updateRole(String id, Role newRole);

    boolean existsByEmail(String email);
}
