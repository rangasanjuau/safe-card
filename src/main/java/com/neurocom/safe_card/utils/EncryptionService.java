package com.neurocom.safe_card.utils;


public interface EncryptionService {

    record CipherRecord(byte[] iv, byte[] ciphertext) {}

    CipherRecord encrypt(byte[] plaintext);
    byte[] decrypt(byte[] iv, byte[] ciphertext);
}