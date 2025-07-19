package com.event.booking.eventservice.exception;

import com.event.booking.eventservice.exception.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class UserUnAuthenticatedException extends EventServiceException{
    public UserUnAuthenticatedException(ExceptionCode code, String message, HttpStatus status) {
        super(code, message, status);
    }
}
