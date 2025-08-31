package com.neurocom.safe_card.exception;

import java.security.GeneralSecurityException;

/**
 * Custom exception for encryption-related errors.
 */
public class EncryptionException extends GeneralSecurityException {
    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}