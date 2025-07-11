package com.event.booking.userservice.exception;

import com.event.booking.userservice.exception.enums.ExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserServiceException extends RuntimeException {
    private final ExceptionCode code;
    private final HttpStatus status;

    public UserServiceException(ExceptionCode code, String message, HttpStatus status){
        super(message);
        this.code = code;
        this.status = status;
    }
}
