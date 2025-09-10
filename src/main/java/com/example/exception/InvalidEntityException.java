package com.example.exception;

/**
 * Exception class for when the entity has an invalid class
 */
public class InvalidEntityException extends RuntimeException {
    public InvalidEntityException(String message) {
        super(message);
    }
}
