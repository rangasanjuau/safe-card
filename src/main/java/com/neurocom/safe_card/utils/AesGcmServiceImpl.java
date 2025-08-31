package com.neurocom.safe_card.utils;

import com.neurocom.safe_card.config.CryptoConfig;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;


/** AES-GCM encryption service.
 *  Uses a key provided in the CryptoConfig.
 */
@Component
public class AesGcmServiceImpl implements EncryptionService {
    private final byte[] key;
    private final SecureRandom random = SecureRandom.getInstanceStrong();

    private static final String AES_ALGO = "AES";


    public AesGcmServiceImpl(CryptoConfig cfg) throws Exception {

        // Get the decoded AES key from config
        byte[] decodedKey = Base64.getDecoder().decode(cfg.getAesKeyBase64());
        // Ensure AES-256
        if (decodedKey.length != 32) throw new IllegalStateException("AES key must be 32 bytes (AES-256)");

        this.key = decodedKey;
    }

    /** Encrypts the given plaintext using AES-GCM.
     * @param plaintext The plaintext to encrypt.
     * @return A CipherRecord containing the IV and ciphertext.
     * @throws GeneralSecurityException on encryption errors.
     */
    @Override
    public CipherRecord encrypt(byte[] plaintext) throws GeneralSecurityException {
        byte[] iv = new byte[12]; // 96-bit IV for GCM
        random.nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, AES_ALGO), new GCMParameterSpec(128, iv));
        byte[] ct = cipher.doFinal(plaintext);
        return new CipherRecord(iv, ct);
    }

    /** Decrypts the given ciphertext using AES-GCM.
     * @param iv The initialization vector used during encryption.
     * @param ciphertext The ciphertext to decrypt.
     * @return The decrypted plaintext.
     * @throws Exception on decryption errors.
     */
    @Override
    public byte[] decrypt(byte[] iv, byte[] ciphertext) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, AES_ALGO), new GCMParameterSpec(128, iv));
        return cipher.doFinal(ciphertext);
    }
}