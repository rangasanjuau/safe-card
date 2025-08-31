package com.neurocom.safe_card.exception;

/**
 * Custom exception for not found errors.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}