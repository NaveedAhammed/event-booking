package com.event.booking.userservice.exception;

import com.event.booking.userservice.exception.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class InvalidAccessTokenException extends UserServiceException{
    public InvalidAccessTokenException(ExceptionCode code, String message, HttpStatus status) {
        super(code, message, status);
    }
}
