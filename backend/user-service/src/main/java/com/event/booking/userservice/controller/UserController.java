package com.event.booking.userservice.controller;

import com.event.booking.userservice.dto.LoginRequest;
import com.event.booking.userservice.dto.OtpRequest;
import com.event.booking.userservice.dto.OtpVerifyRequest;
import com.event.booking.userservice.dto.RegisterRequest;
import com.event.booking.userservice.service.OtpService;
import com.event.booking.userservice.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Value("${frontend.redirect.url}")
    private String FRONTEND_REDIRECT_URL;


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        log.info("RegisterRequest: {}", request);

        Map<String, String> tokens = userService.register(request);

        setRefreshTokenCookie(response, tokens.get("refresh_token"));

        return new ResponseEntity<>(Map.of("access_token", tokens.get("access_token")), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        log.info("LoginRequest: {}", request);

        Map<String, String> tokens = userService.login(request);

        setRefreshTokenCookie(response,tokens.get("refresh_token"));

        return new ResponseEntity<>(Map.of("access_token", tokens.get("access_token")), HttpStatus.OK);
    }

    @GetMapping("/oauth/google")
    public void handleGoogleOAuth(@RequestParam String code, HttpServletResponse response) throws IOException {
        log.info("Google oauth code: {}", code);

        Map<String, String> tokens = userService.oAuth(code);

        setRefreshTokenCookie(response, tokens.get("refresh_token"));

        response.sendRedirect(FRONTEND_REDIRECT_URL + tokens.get("access_token"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@CookieValue("refresh_token") String refreshToken){
        String accessToken = userService.refresh(refreshToken);
        return ResponseEntity.ok(Map.of("access_token", accessToken));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Void> sendOtp(@Valid @RequestBody OtpRequest request){
        userService.sendOtp(request.getMobile());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtpAndLogin(@Valid @RequestBody OtpVerifyRequest request, HttpServletResponse response) {
        log.info("OtpVerifyRequest: {}", request);

        Map<String, String> tokens = userService.verifyOtp(request);

        setRefreshTokenCookie(response,tokens.get("refresh_token"));

        return new ResponseEntity<>(Map.of("access_token", tokens.get("access_token")), HttpStatus.OK);
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken){
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in prod
        cookie.setPath("/");

        response.addCookie(cookie);

        log.info("Added refresh_token cookie");
    }
}
