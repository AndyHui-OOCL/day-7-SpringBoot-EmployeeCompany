package org.example.demo.controller;

import org.example.demo.service.EmployeeInactiveException;
import org.example.demo.service.EmployeeNotFoundException;
import org.example.demo.service.InvalidEmployeeCreationCriteriaException;
import org.example.demo.service.InvalidPaginationNumberException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidEmployeeCreationCriteriaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmployeeCreationCriteriaException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleEmployeeNotFoundException(Exception e) {
        System.out.println(e.getMessage());
    }

    @ExceptionHandler(EmployeeInactiveException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleEmployeeInactiveException(Exception e) {System.out.println(e.getMessage());}

    @ExceptionHandler(InvalidPaginationNumberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidPaginationNumber(Exception e) {return e.getMessage();}
}
