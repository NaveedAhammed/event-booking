package com.ecommerce.customerservice.service;

import com.ecommerce.customerservice.exception.CustomerNotFoundException;
import com.ecommerce.customerservice.model.Customer;
import com.ecommerce.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.ecommerce.customerservice.constant.Constants.*;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_WITH_EMAIL + COLON + email));

        return User.withUsername(customer.getEmail())
                .password("")
                .authorities(Collections.emptyList())
                .build();
    }
}
