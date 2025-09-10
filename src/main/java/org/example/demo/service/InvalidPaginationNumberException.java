package org.example.demo.service;

public class InvalidPaginationNumberException extends RuntimeException {
    public InvalidPaginationNumberException(String message) {
        super(message);
    }
}
