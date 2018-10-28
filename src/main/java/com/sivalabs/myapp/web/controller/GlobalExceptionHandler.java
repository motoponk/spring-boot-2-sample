package com.sivalabs.myapp.web.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { EmptyResultDataAccessException.class })
    ResponseEntity handleEmptyResultDataAccessException(Exception exception) {
        return new ResponseEntity(exception, HttpStatus.NOT_FOUND);
    }
}
