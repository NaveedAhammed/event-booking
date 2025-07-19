package com.event.booking.userservice.model.enums;

import com.event.booking.userservice.exception.InvalidRoleException;

import java.util.Arrays;

public enum Role {
    USER,
    ORGANIZER,
    ADMIN;

    public static Role fromValue(String value){
        return Arrays.stream(values())
                .filter(role -> role.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new InvalidRoleException("Invalid role: " + value));
    }
}
