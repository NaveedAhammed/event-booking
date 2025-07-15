package com.event.booking.userservice.service;

import com.event.booking.userservice.dto.*;
import com.event.booking.userservice.model.User;
import com.event.booking.userservice.model.enums.Role;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, String> register(RegisterRequest request);

    Map<String, String> login(LoginRequest request);

    Map<String, String> oAuth(String code);

    String refresh(String refreshToken);

    boolean existsByEmail(String email);

    void sendOtp(String mobile);

    Map<String, String> verifyOtp(OtpVerifyRequest request);
}
