package com.event.booking.userservice.exception;

import com.event.booking.userservice.exception.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class UserUnAuthorizedException extends UserServiceException{
    public UserUnAuthorizedException(ExceptionCode code, String message, HttpStatus status) {
        super(code, message, status);
    }
}
