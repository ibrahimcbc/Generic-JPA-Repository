package com.example.exception;

/**
 * Exception class for when the pagination methods have invalid values
 */
public class InvalidPaginationException extends RuntimeException {
    public InvalidPaginationException(String message) {}
}
