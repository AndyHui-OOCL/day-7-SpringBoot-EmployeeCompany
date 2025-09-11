package org.example.demo.exception;

public class InvalidPaginationNumberException extends RuntimeException {
    public InvalidPaginationNumberException(String message) {
        super(message);
    }
}
