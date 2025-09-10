package org.example.demo.service;

public class EmployeeInactiveException extends RuntimeException {
    public EmployeeInactiveException(String message) {
        super(message);
    }
}
