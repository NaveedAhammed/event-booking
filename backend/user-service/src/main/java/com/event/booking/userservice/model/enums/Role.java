package com.event.booking.userservice.model.enums;

import com.event.booking.userservice.exception.InvalidRoleException;

import java.util.Arrays;

public enum Role {
    USER("USER"),
    ORGANIZER("ORGANIZER"),
    ADMIN("ADMIN");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    private static Role fromValue(String value){
        return Arrays.stream(values())
                .filter(role -> role.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new InvalidRoleException("Invalid role value: " + value));
    }
}
