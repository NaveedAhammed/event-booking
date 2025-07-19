package com.event.booking.eventservice.exception;

import com.event.booking.eventservice.exception.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class InvalidEventTypeException extends EventServiceException{
    public InvalidEventTypeException(String message) {
        super(ExceptionCode.INVALID_EVENT_TYPE, message, HttpStatus.BAD_REQUEST);
    }
}
