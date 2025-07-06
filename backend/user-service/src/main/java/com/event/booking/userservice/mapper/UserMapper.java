package com.event.booking.userservice.mapper;

import com.event.booking.userservice.dto.AuthResponse;
import com.event.booking.userservice.dto.RegisterRequest;
import com.event.booking.userservice.dto.UserResponse;
import com.event.booking.userservice.model.User;
import com.event.booking.userservice.model.enums.AuthProvider;
import com.event.booking.userservice.model.enums.Role;

public class UserMapper {
    public static UserResponse toUserResponse(User user) {
        if (user == null) return null;

        return UserResponse.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .authProvider(user.getAuthProvider().name())
                .createdAt(user.getCreatedAt().toString())
                .build();
    }

    public static User toUser(RegisterRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(Role.valueOf(request.getRole()))
                .authProvider(AuthProvider.LOCAL)
                .build();
    }

    public static AuthResponse toAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .userResponse(toUserResponse(user))
                .token(token)
                .build();
    }
}
