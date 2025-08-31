package com.neurocom.safe_card.exception;

/**
 * Custom exception for invalid PAN (Primary Account Number) errors.
 */
public class InvalidPanException extends RuntimeException {
    public InvalidPanException(String message) {
        super(message);
    }
}