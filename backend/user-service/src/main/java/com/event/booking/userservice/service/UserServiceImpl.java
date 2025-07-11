package com.event.booking.userservice.service;

import com.event.booking.userservice.dto.LoginRequest;
import com.event.booking.userservice.dto.RegisterRequest;
import com.event.booking.userservice.exception.InvalidCredentials;
import com.event.booking.userservice.exception.UserAlreadyExistsException;
import com.event.booking.userservice.exception.UserNotFoundException;
import com.event.booking.userservice.mapper.UserMapper;
import com.event.booking.userservice.model.User;
import com.event.booking.userservice.model.enums.AuthProvider;
import com.event.booking.userservice.model.enums.Role;
import com.event.booking.userservice.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_WITH_EMAIL + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
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

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(GOOGLE_APIS_TOKEN_URL, request, Map.class);

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

    private Map<String, String> generateTokens(User user) {
        String accessToken = jwtService.generateToken(user, false);
        String refreshToken = jwtService.generateToken(user, true);

        Map<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("refresh_token", refreshToken);

        return map;
    }
}
