package org.example.demo.exception;

public class InvalidEmployeeCreationCriteriaException extends RuntimeException {
    public InvalidEmployeeCreationCriteriaException(String message) {
        super(message);
    }
}
