package com.example.lab_1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exc) {
        // create a Error response
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, exc.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }
}
