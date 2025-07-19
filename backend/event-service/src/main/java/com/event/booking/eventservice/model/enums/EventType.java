package com.event.booking.eventservice.model.enums;

import com.event.booking.eventservice.exception.InvalidEventTypeException;

import java.util.Arrays;

public enum EventType {
    COMEDY,
    MUSIC,
    SPORTS,
    WORKSHOP,
    EXHIBITION,
    CONCERT,
    CONFERENCE,
    GAMING,
    PLAYS;

    public static EventType fromValue(String value){
        return Arrays.stream(values())
                .filter(eventType -> eventType.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new InvalidEventTypeException("Invalid event type: " + value));
    }
}
