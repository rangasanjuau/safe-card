package com.neurocom.safe_card.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for cryptographic keys.
 * Expects base64-encoded AES and HMAC keys in application properties.
 */
@Configuration
@ConfigurationProperties(prefix = "secret")
public class CryptoConfig {
    private String aesKeyBase64; // 32 bytes (AES-256)
    private String hmacKeyBase64; // HMAC-SHA-256 key
    public String getAesKeyBase64() { return aesKeyBase64; }
    public void setAesKeyBase64(String v) { this.aesKeyBase64 = v; }
    public String getHmacKeyBase64() { return hmacKeyBase64; }
    public void setHmacKeyBase64(String v) { this.hmacKeyBase64 = v; }
}