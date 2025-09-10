package com.example.exception;

/**
 * Exception class for when the entity not found
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
