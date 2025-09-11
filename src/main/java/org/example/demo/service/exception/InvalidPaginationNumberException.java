package org.example.demo.service.exception;

public class InvalidPaginationNumberException extends RuntimeException {
    public InvalidPaginationNumberException(String message) {
        super(message);
    }
}
