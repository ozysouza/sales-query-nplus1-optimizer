package com.desouza.app.controllers.handlers;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.desouza.app.dto.error.CustomError;
import com.desouza.app.service.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomError err = new CustomError(e.getMessage(), status.value(), request.getRequestURI(), Instant.now());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<CustomError> DateTimeParseException(DateTimeParseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        CustomError err = new CustomError("Data format should be YYYY-MM-DD", status.value(), request.getRequestURI(),
                Instant.now());
        return ResponseEntity.status(status).body(err);
    }

}