package com.neurocom.safe_card.utils;

import java.security.GeneralSecurityException;

public interface EncryptionService {

    record CipherRecord(byte[] iv, byte[] ciphertext) {}

    CipherRecord encrypt(byte[] plaintext) throws GeneralSecurityException;
    byte[] decrypt(byte[] iv, byte[] ciphertext) throws Exception;
}