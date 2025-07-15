package com.event.booking.userservice.exception;

import com.event.booking.userservice.exception.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class OtpSendException extends UserServiceException{
    public OtpSendException(ExceptionCode code, String message, HttpStatus status) {
        super(code, message, status);
    }
}
