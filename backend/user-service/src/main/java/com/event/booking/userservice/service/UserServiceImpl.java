package com.event.booking.userservice.service;

import com.event.booking.userservice.dto.LoginRequest;
import com.event.booking.userservice.dto.OtpVerifyRequest;
import com.event.booking.userservice.dto.RegisterRequest;
import com.event.booking.userservice.exception.*;
import com.event.booking.userservice.exception.enums.ExceptionCode;
import com.event.booking.userservice.mapper.UserMapper;
import com.event.booking.userservice.model.User;
import com.event.booking.userservice.model.enums.AuthProvider;
import com.event.booking.userservice.model.enums.Role;
import com.event.booking.userservice.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.event.booking.userservice.constant.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final RestTemplate restTemplate;

    private final OtpService otpService;

    @Value("${google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${backend.redirect.url}")
    private String REDIRECT_URI;

    @Value("${google.apis.token.url}")
    private String GOOGLE_APIS_TOKEN_URL;

    @Override
    public Map<String, String> register(RegisterRequest request) {
        String email = request.getEmail();
        if (existsByEmail(email)){
            log.error(USER_ALREADY_EXISTS_WITH_EMAIL + COLON + CURLY_PLACEHOLDER, email);
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_WITH_EMAIL + COLON + email);
        }

        User user = UserMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);

        log.info("New user registered with email: {}", user.getEmail());

        return generateTokens(user);
    }

    @Override
    public Map<String, String> login(LoginRequest request) {
        String email = request.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error(USER_NOT_FOUND_WITH_EMAIL + COLON + CURLY_PLACEHOLDER, email);
                    return new UserNotFoundException(USER_NOT_FOUND_WITH_EMAIL + email);
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            log.error(INVALID_CREDENTIALS);
            throw new InvalidCredentials(INVALID_CREDENTIALS);
        }

        log.info("Login successful for user: {}", user.getEmail());

        return generateTokens(user);
    }

    @Override
    public Map<String, String> oAuth(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", GOOGLE_CLIENT_ID);
        params.add("client_secret", GOOGLE_CLIENT_SECRET);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        log.info("Google token request: {}", request);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(GOOGLE_APIS_TOKEN_URL, request, Map.class);

        if (tokenResponse.getBody() == null) {
            throw new UserServiceException(ExceptionCode.INTERNAL_SERVICE_ERROR, INTERNAL_SERVICE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Google token response: {}", tokenResponse);

        String idToken = (String) tokenResponse.getBody().get("id_token");

        GoogleIdToken.Payload payload = jwtService.verifyGoogleToken(idToken);
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        User user = userRepository.findByEmail(email).orElse(null);

        if(user == null){
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setAuthProvider(AuthProvider.GOOGLE);
            user.setRole(Role.USER);
            user = userRepository.save(user);
        }

        return generateTokens(user);
    }

    @Override
    public String refresh(String refreshToken) {
        if (jwtService.validateJwtToken(refreshToken, true)){
            String email = jwtService.extractUsername(refreshToken, true);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_WITH_EMAIL + email));

            return jwtService.generateToken(user, false);
        }
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void sendOtp(String mobile) {
        if (!userRepository.existsByMobile(mobile)){
            log.error(NO_USER_REGISTERED_WITH_MOBILE + COLON + CURLY_PLACEHOLDER, mobile);
            throw new UserNotFoundException(NO_USER_REGISTERED_WITH_MOBILE + COLON + mobile);
        }

        otpService.generateAndStoreOtp(mobile);
    }

    @Override
    public Map<String, String> verifyOtp(OtpVerifyRequest request) {
        boolean valid = otpService.validateOtp(request.getMobile(), request.getOtp());

        if (!valid) {
            log.error(INVALID_OTP + COLON + CURLY_PLACEHOLDER, request.getOtp());
            throw new InvalidOtpException(ExceptionCode.INVALID_OTP, INVALID_OTP, HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findByMobile(request.getMobile())
                .orElseThrow(() -> {
                    log.error(NO_USER_REGISTERED_WITH_MOBILE + COLON + CURLY_PLACEHOLDER, request.getMobile());
                    return new UserNotFoundException(NO_USER_REGISTERED_WITH_MOBILE + COLON + request.getMobile());
                });

        return generateTokens(user);
    }

    private Map<String, String> generateTokens(User user) {
        String accessToken = jwtService.generateToken(user, false);
        String refreshToken = jwtService.generateToken(user, true);

        Map<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("refresh_token", refreshToken);

        return map;
    }
}
