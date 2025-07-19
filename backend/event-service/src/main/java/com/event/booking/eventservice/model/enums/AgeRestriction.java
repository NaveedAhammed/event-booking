package com.event.booking.eventservice.model.enums;

import com.event.booking.eventservice.exception.InvalidEventTypeException;

import java.util.Arrays;

public enum AgeRestriction {
    ALL,
    EIGHTEEN_PLUS;

    public static AgeRestriction fromValue(String value){
        return Arrays.stream(values())
                .filter(ageRestriction -> ageRestriction.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new InvalidEventTypeException("Invalid age restriction: " + value));
    }
}
