package com.event.booking.userservice.service;

import com.event.booking.userservice.exception.UserServiceException;
import com.event.booking.userservice.exception.enums.ExceptionCode;
import com.event.booking.userservice.model.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

import static com.event.booking.userservice.constant.Constants.*;

@Slf4j
@Service
public class JwtService {
    @Value("${jwt.access.token.secret}")
    private String ACCESS_SECRET;

    @Value("${jwt.refresh.token.secret}")
    private String REFRESH_SECRET;

    @Value("${jwt.access.token.expiration}")
    private long ACCESS_EXPIRATION;

    @Value("${jwt.refresh.token.expiration}")
    private long REFRESH_EXPIRATION;

    private Key cachedKey;

    private final GoogleIdTokenVerifier verifier;

    public JwtService(
            @Value("${google.client-id}") String GOOGLE_CLIENT_ID
    ) throws GeneralSecurityException, IOException {
        this.verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();
    }

    public GoogleIdToken.Payload verifyGoogleToken(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null){
                log.error("Null idToken");
                throw new UserServiceException(ExceptionCode.INTERNAL_SERVICE_ERROR,INTERNAL_SERVICE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return idToken.getPayload();
        }catch (Exception e) {
            log.error("Token verification failed: {}", e.getMessage());
            throw new UserServiceException(ExceptionCode.INTERNAL_SERVICE_ERROR,INTERNAL_SERVICE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.info("Authorization: {}", bearerToken);

        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }

        return null;
    }

    public String generateToken(User user, boolean isRefreshToken) {
        long expiration = isRefreshToken ? REFRESH_EXPIRATION : ACCESS_EXPIRATION;

        return Jwts.builder()
                .claim(ROLE, user.getRole().name())
                .claim(USER_ID, user.getId())
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key(isRefreshToken))
                .compact();
    }

    public String extractUsername(String token, boolean isRefreshToken) {
        return extractClaim(token, Claims::getSubject, isRefreshToken);
    }

    public String extractRole(String token, boolean isRefreshToken) {
        return extractClaim(token, claims -> claims.get(ROLE, String.class), isRefreshToken);
    }

    public boolean validateJwtToken(String token, boolean isRefreshToken) {
        try {
            log.info("Validating token: {}", token);

            if (token == null) return false;

            Jwts.parser()
                    .verifyWith((SecretKey) key(isRefreshToken))
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (Exception e) {
            log.error(INVALID_JWT_TOKEN + COLON + CURLY_PLACEHOLDER, e.getMessage());
            return false;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, boolean isRefreshToken) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) key(isRefreshToken))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    private Key key(boolean isRefreshToken) {
        String secret = isRefreshToken ? REFRESH_SECRET : ACCESS_SECRET;
        if (cachedKey == null) {
            cachedKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
        return cachedKey;
    }
}
