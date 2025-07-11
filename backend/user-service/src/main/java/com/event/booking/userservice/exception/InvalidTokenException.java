package com.event.booking.userservice.exception;

import com.event.booking.userservice.exception.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends UserServiceException {
    public InvalidTokenException(String message){
        super(ExceptionCode.INVALID_TOKEN, message, HttpStatus.UNAUTHORIZED);
    }
}
