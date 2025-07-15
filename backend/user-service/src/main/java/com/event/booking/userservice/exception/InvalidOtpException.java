package com.event.booking.userservice.exception;

import com.event.booking.userservice.exception.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class InvalidOtpException extends UserServiceException{

    public InvalidOtpException(ExceptionCode code, String message, HttpStatus status) {
        super(code, message, status);
    }
}
