package com.example.exception;

/**
 * Exception class for when the id value is invalid
 */
public class InvalidIdException extends RuntimeException {
    public InvalidIdException(String message) {
        super(message);
    }
}
