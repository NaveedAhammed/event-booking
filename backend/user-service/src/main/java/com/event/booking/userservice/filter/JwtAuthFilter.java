package com.event.booking.userservice.filter;

import com.event.booking.userservice.exception.InvalidTokenException;
import com.event.booking.userservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_URLS = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/oauth/google/callback",
            "/favicon.ico",
            "/api/auth/refresh"
    );

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        log.info("JwtAuthFilter called for URI: {}", requestURI);

        if (PUBLIC_URLS.contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = jwtService.getJwtFromHeader(request);

            if (SecurityContextHolder.getContext().getAuthentication() == null && jwtService.validateJwtToken(token, false)) {
                String email = jwtService.extractUsername(token, false);
                String role = jwtService.extractRole(token, false);

                GrantedAuthority authority = new SimpleGrantedAuthority(role);

                UserDetails userDetails = User.withUsername(email)
                        .password("")
                        .roles(authority.getAuthority())
                        .build();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (InvalidTokenException ex){
            log.error("JWT Validation Error: {}", ex.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    String.format("{\"message\": \"%s\"}", ex.getMessage())
            );
        }
    }
}
