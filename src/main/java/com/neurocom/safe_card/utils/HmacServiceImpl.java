package com.neurocom.safe_card.utils;


import com.neurocom.safe_card.config.CryptoConfig;
import org.springframework.stereotype.Component;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/** HMAC service for computing HMAC-SHA-256 hashes.
 *  Uses a key provided in the CryptoConfig.
 */

@Component
public class HmacServiceImpl implements HmacService {

    public static final String HMAC_SHA_256 = "HmacSHA256";

    private final Mac mac;
    public HmacServiceImpl(CryptoConfig cfg) throws Exception {

        // Get the decoded Hmac key from config
        byte[] k = Base64.getDecoder().decode(cfg.getHmacKeyBase64());

        // Create SecretKeySpec and Mac instance
        SecretKeySpec key = new SecretKeySpec(k, HMAC_SHA_256);
        this.mac = Mac.getInstance(HMAC_SHA_256);
        this.mac.init(key);
    }

    /** Computes the HMAC-SHA-256 of the given normalized PAN and returns it as a hex string.
     * @param normalizedPan The normalized PAN (e.g. digits only, no spaces).
     * @return The HMAC-SHA-256 as a hex string.
     */
    @Override
    public String getHmacHex(String normalizedPan) {
        //
        byte[] out = mac.doFinal(normalizedPan.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder(out.length*2);
        for (byte b : out) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}