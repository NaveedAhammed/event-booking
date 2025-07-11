package com.event.booking.userservice.exception;

import com.event.booking.userservice.exception.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class InvalidCredentials extends UserServiceException {
    public InvalidCredentials(String message){
        super(ExceptionCode.INVALID_CREDENTIALS, message, HttpStatus.UNAUTHORIZED);
    }
}
