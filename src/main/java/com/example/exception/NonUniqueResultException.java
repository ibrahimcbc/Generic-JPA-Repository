package com.example.exception;

/**
 * Exception class for when methods returns more than one result
 */
public class NonUniqueResultException extends RuntimeException {
    public NonUniqueResultException(String message) {
        super(message);
    }
}
