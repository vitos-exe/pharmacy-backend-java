package com.example.demo.controller.adviser;

import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public void NotSuchElementExceptionHandler(){}

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, PropertyValueException.class})
    public void IllegalArgumentExceptionHandler(){}

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(SecurityException.class)
    public void SecurityException(){}
}
