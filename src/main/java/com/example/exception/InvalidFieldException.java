package com.example.exception;

/**
 * Exception class for when the field value is invalid
 */
public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException(String message) {
        super(message);
    }
}
