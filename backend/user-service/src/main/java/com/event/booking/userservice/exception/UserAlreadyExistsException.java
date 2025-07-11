package com.event.booking.userservice.exception;

import com.event.booking.userservice.exception.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends UserServiceException {
    public UserAlreadyExistsException(String message){
        super(ExceptionCode.USER_ALREADY_EXISTS, message, HttpStatus.CONFLICT);
    }
}
