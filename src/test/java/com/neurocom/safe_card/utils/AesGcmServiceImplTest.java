package com.neurocom.safe_card.utils;

import com.neurocom.safe_card.config.CryptoConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AesGcmServiceImplTest {

    public static final String TEST_PLAIN_TEXT = "Test Encryption Decryption";
    private AesGcmServiceImpl aesService;

    @BeforeEach
    void setup() throws Exception {
        // Mock CryptoConfig
        CryptoConfig mockConfig = Mockito.mock(CryptoConfig.class);

        // Generate random 32-byte AES key (AES-256)
        String base64Key = generateAESKey();

        when(mockConfig.getAesKeyBase64()).thenReturn(base64Key);

        // Create the AES service
        aesService = new AesGcmServiceImpl(mockConfig);
    }

    public String generateAESKey() throws Exception
    {
        SecretKey key = KeyGenerator.getInstance("AES").generateKey();
        byte[] keyBytes = key.getEncoded();
        if (keyBytes.length != 32) {
            // pad/truncate to 32 bytes for test
            byte[] tmp = new byte[32];
            System.arraycopy(keyBytes, 0, tmp, 0, Math.min(keyBytes.length, 32));
            keyBytes = tmp;
        }
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    @Test
    void testEncryptDecrypt() throws Exception {
        byte[] plaintext = TEST_PLAIN_TEXT.getBytes();

        // Encrypt
        EncryptionService.CipherRecord cipherRecord = aesService.encrypt(plaintext);

        // Decrypt
        byte[] decrypted = aesService.decrypt(cipherRecord.iv(), cipherRecord.ciphertext());

        // Must match original
        assertArrayEquals(plaintext, decrypted, "Decrypted plaintext must match original");
    }

    @Test
    void testEncryptProducesDifferentCiphertexts() throws Exception {
        byte[] plaintext = TEST_PLAIN_TEXT.getBytes();

        EncryptionService.CipherRecord record1 = aesService.encrypt(plaintext);
        EncryptionService.CipherRecord record2 = aesService.encrypt(plaintext);

        // IV ensures ciphertext differs
        assertFalse(java.util.Arrays.equals(record1.ciphertext(), record2.ciphertext()),
                "Ciphertext should differ due to random IV");
    }

    @Test
    void testInvalidKeyLength_throwsException() {
        CryptoConfig badConfig = Mockito.mock(CryptoConfig.class);
        when(badConfig.getAesKeyBase64()).thenReturn(Base64.getEncoder().encodeToString(new byte[16])); // too short

        Exception ex = assertThrows(IllegalStateException.class, () -> new AesGcmServiceImpl(badConfig));
        assertEquals("AES key must be 32 bytes (AES-256)", ex.getMessage());
    }
}
