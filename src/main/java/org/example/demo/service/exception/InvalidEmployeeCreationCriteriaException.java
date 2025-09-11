package org.example.demo.service.exception;

public class InvalidEmployeeCreationCriteriaException extends RuntimeException {
    public InvalidEmployeeCreationCriteriaException(String message) {
        super(message);
    }
}
