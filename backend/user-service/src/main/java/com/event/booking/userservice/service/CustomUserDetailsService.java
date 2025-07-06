package com.event.booking.userservice.service;

import com.event.booking.userservice.exception.UserNotFoundException;
import com.event.booking.userservice.model.User;
import com.event.booking.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.event.booking.userservice.constant.Constants.COLON;
import static com.event.booking.userservice.constant.Constants.USER_NOT_FOUND_WITH_EMAIL;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_WITH_EMAIL + COLON + email));

        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password("")
                .authorities(Collections.emptyList())
                .build();
    }
}
