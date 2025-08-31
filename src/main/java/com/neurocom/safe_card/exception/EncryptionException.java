package com.neurocom.safe_card.exception;

/**
 * Custom exception for encryption-related errors.
 */
public class EncryptionException extends RuntimeException {
    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}