package com.job.publisher.controller;

import com.job.publisher.dto.WebResponse;
import com.job.publisher.exception.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;

@Component
@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> constraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder().errors(exception.getMessage()).build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> apiException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(WebResponse.<String>builder().errors(exception.getReason()).build());
    }


    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<WebResponse<String>> invalidTokenException(InvalidTokenException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(WebResponse.<String>builder().errors(exception.getMessage()).build());
    }
}
