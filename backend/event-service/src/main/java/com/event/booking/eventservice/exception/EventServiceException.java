package com.event.booking.eventservice.exception;

import com.event.booking.eventservice.exception.enums.ExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EventServiceException extends RuntimeException{
    private final ExceptionCode code;
    private final HttpStatus status;

    public EventServiceException(ExceptionCode code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }
}
