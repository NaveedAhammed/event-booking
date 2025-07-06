package com.event.booking.userservice.service;

import com.event.booking.userservice.dto.AuthResponse;
import com.event.booking.userservice.dto.LoginRequest;
import com.event.booking.userservice.dto.RegisterRequest;
import com.event.booking.userservice.dto.UserResponse;
import com.event.booking.userservice.model.enums.Role;

import java.util.List;

public interface UserService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse processOAuthLogin(String email, String name);

    UserResponse getProfile();

    List<UserResponse> getAllUsers();

    void updateRole(String id, Role newRole);

    boolean existsByEmail(String email);
}
