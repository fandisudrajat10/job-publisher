package com.job.publisher.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends RuntimeException {

    private final HttpStatus httpStatus;

    public InvalidTokenException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
