package com.event.booking.userservice.controller;

import com.event.booking.userservice.dto.AuthResponse;
import com.event.booking.userservice.dto.LoginRequest;
import com.event.booking.userservice.dto.RegisterRequest;
import com.event.booking.userservice.dto.UserResponse;
import com.event.booking.userservice.model.User;
import com.event.booking.userservice.service.JwtService;
import com.event.booking.userservice.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final RestTemplate restTemplate;

    private final JwtService jwtService;

    @Value("${google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${backend.redirect.url}")
    private String REDIRECT_URI;

    @Value("${google.apis.token.url}")
    private String GOOGLE_APIS_TOKEN_URL;

    @Value("${frontend.redirect.url}")
    private String FRONTEND_REDIRECT_URL;


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        Map<String, String> tokens = userService.register(request);

        Cookie cookie = new Cookie("refresh_token", tokens.get("refresh_token"));
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in prod
        cookie.setPath("/");

        response.addCookie(cookie);

        return new ResponseEntity<>(Map.of("access_token", tokens.get("access_token")), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        Map<String, String> tokens = userService.login(request);

        Cookie cookie = new Cookie("refresh_token", tokens.get("refresh_token"));
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in prod
        cookie.setPath("/");

        response.addCookie(cookie);

        return new ResponseEntity<>(Map.of("access_token", tokens.get("access_token")), HttpStatus.OK);
    }

    @GetMapping("/oauth/google/callback")
    public void handleGoogleCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", GOOGLE_CLIENT_ID);
        params.add("client_secret", GOOGLE_CLIENT_SECRET);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(GOOGLE_APIS_TOKEN_URL, request, Map.class);

        String idToken = (String) tokenResponse.getBody().get("id_token");

        GoogleIdToken.Payload payload = jwtService.verifyGoogleToken(idToken);
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        Map<String, String> tokens = userService.oAuth(email, name);

        Cookie cookie = new Cookie("refresh_token", tokens.get("refresh_token"));
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in prod
        cookie.setPath("/");

        response.addCookie(cookie);

        response.sendRedirect(FRONTEND_REDIRECT_URL + tokens.get("access_token"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@CookieValue("refresh_token") String refreshToken){
        String accessToken = userService.refresh(refreshToken);
        return ResponseEntity.ok(Map.of("access_token", accessToken));
    }
}
