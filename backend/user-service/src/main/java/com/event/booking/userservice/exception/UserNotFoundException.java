package com.event.booking.userservice.exception;

import com.event.booking.userservice.exception.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserServiceException {
    public UserNotFoundException(String message){
        super(ExceptionCode.USER_NOT_FOUND, message, HttpStatus.NOT_FOUND);
    }
}
