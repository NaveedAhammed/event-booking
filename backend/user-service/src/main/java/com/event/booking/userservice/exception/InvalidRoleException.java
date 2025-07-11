package com.event.booking.userservice.exception;

import com.event.booking.userservice.exception.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class InvalidRoleException extends UserServiceException {
    public InvalidRoleException(String message){
        super(ExceptionCode.INVALID_ROLE, message, HttpStatus.FORBIDDEN);
    }
}
