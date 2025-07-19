package com.event.booking.eventservice.exception;

import com.event.booking.eventservice.exception.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class InvalidAgeRestrictionException extends EventServiceException {
    public InvalidAgeRestrictionException(ExceptionCode code, String message, HttpStatus status) {
        super(ExceptionCode.INVALID_AGE_RESTRICTION, message, HttpStatus.BAD_REQUEST);
    }
}
