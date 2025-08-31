package com.neurocom.safe_card.utils;

import com.neurocom.safe_card.config.CryptoConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

class HmacServiceTest {

    public static final String PAN = "4539148803436467";

    private HmacServiceImpl hmacService;

    @BeforeEach
    void setup() throws Exception {
        // Mock CryptoConfig
        CryptoConfig mockConfig = Mockito.mock(CryptoConfig.class);

        // Generate a random HMAC key for testing
        SecretKey key = KeyGenerator.getInstance("HmacSHA256").generateKey();
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

        // Configure mock to return the Base64 key
        when(mockConfig.getHmacKeyBase64()).thenReturn(base64Key);

        // Create HmacServiceImpl instance
        hmacService = new HmacServiceImpl(mockConfig);
    }

    @Test
    void testGetHmacHex_sameInput_returnsSameOutput() {

        String hmac1 = hmacService.getHmacHex(PAN);
        String hmac2 = hmacService.getHmacHex(PAN);

        assertEquals(hmac1, hmac2, "HMAC should be same for same input");
    }

    @Test
    void testGetHmacHex_differentInput_returnsDifferentOutput() {

        String pan2 = "4556737586899855";

        String hmac1 = hmacService.getHmacHex(PAN);
        String hmac2 = hmacService.getHmacHex(pan2);

        assertNotEquals(hmac1, hmac2, "HMAC should differ for different input");
    }

    @Test
    void testGetHmacHex_outputLengthIsCorrect() {

        String hmac = hmacService.getHmacHex(PAN);

        // HmacSHA256 output = 32 bytes = 64 hex chars
        assertEquals(64, hmac.length(), "HMAC hex string should be 64 characters");
    }

}