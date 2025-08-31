package com.neurocom.safe_card.utils;

public interface EncryptionService {

    record CipherRecord(byte[] iv, byte[] ciphertext) {}

    CipherRecord encrypt(byte[] plaintext) throws Exception;
    byte[] decrypt(byte[] iv, byte[] ciphertext) throws Exception;
}