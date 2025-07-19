package com.event.booking.eventservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.UUID;
import java.util.function.Function;

import static com.event.booking.eventservice.constants.Constants.*;

@Slf4j
@Service
public class JwtService {
    @Value("${jwt.access.token.secret}")
    private String ACCESS_SECRET;

    @Value("${jwt.refresh.token.secret}")
    private String REFRESH_SECRET;

    private Key cachedKey;

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.info("Authorization: {}", bearerToken);

        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }

        return null;
    }

    public String extractUsername(String token, boolean isRefreshToken) {
        return extractClaim(token, Claims::getSubject, isRefreshToken);
    }

    public String extractRole(String token, boolean isRefreshToken) {
        return extractClaim(token, claims -> claims.get(ROLE, String.class), isRefreshToken);
    }

    public UUID extractUserId(String token, boolean isRefreshToken) {
        return extractClaim(token, claims -> claims.get(USER_ID, UUID.class), isRefreshToken);
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
