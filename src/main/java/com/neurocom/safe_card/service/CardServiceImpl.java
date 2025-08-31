package com.neurocom.safe_card.service;

import com.neurocom.safe_card.dto.Dtos;
import com.neurocom.safe_card.entity.Card;
import com.neurocom.safe_card.repository.CardRepository;
import com.neurocom.safe_card.utils.EncryptionService;
import com.neurocom.safe_card.utils.HmacService;
import com.neurocom.safe_card.utils.PanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;


/** Implementation of CardService for managing card records.
 *  Handles creation and searching of cards with encrypted PAN storage.
 */
@Service
public class CardServiceImpl implements CardService{
    private final CardRepository repo;
    private final EncryptionService encryptionService;
    private final HmacService hmacService;

    public CardServiceImpl(CardRepository repo, EncryptionService aes, HmacService
            hmac) {
        this.repo = repo;
        this.encryptionService = aes;
        this.hmacService = hmac;
    }

    /**
     * Creates a new card record with encrypted PAN.
     * @param name The cardholder's name.
     * @param rawPan The raw PAN (may include spaces).
     * @return The created CardResponse DTO.
     * @throws Exception on encryption errors or invalid PAN.
     */
    @Override
    @Transactional
    public Dtos.CardResponse create(String name, String rawPan) throws Exception {

        String pan = PanUtils.normalize(rawPan);
        if (!PanUtils.isLengthValid(pan) || !PanUtils.isLuhnValid(pan)) throw new
                IllegalArgumentException("Invalid PAN");

        var pack = encryptionService.encrypt(pan.getBytes(StandardCharsets.UTF_8));

        // Compute HMAC of full PAN and last 4 digits
        String panHmac = hmacService.getHmacHex(pan);
        String last4 = pan.substring(pan.length() - 4);

        // Create card record andd Save to DB
        Card c = new Card();
        c.setCardholderName(name);
        c.setIv(pack.iv());
        c.setPanCiphertext(pack.ciphertext());
        c.setPanHmac(panHmac);
        c.setLast4Hmac(last4);
        Card saved = repo.save(c);
        return new Dtos.CardResponse(saved.getId(), name, PanUtils.maskPan(pan),
                saved.getCreatedAt());
    }

    /**
     * Searches for cards by full PAN.
     * @param rawPan The raw PAN to search for (may include spaces).
     * @return A list of matching CardResponse DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Dtos.CardResponse> searchByPan(String rawPan) {
        String pan = PanUtils.normalize(rawPan);
        String idx = hmacService.getHmacHex(pan);
        return repo.findByPanHmac(idx).stream()
                .map(c -> new Dtos.CardResponse(c.getId(), c.getCardholderName(),
                        PanUtils.maskPan(pan), c.getCreatedAt()))
                .toList();
    }

    /**
     * Searches for cards by last 4 digits of the PAN.
     * @param last4 The last 4 digits to search for.
     * @return A list of matching CardResponse DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Dtos.CardResponse> searchByLast4Digits(String last4) {

        // Seearch by last 4 digits and return a list of masked PANs
        return repo.findByLast4Hmac(last4).stream()
                .map(c -> {
                    try {
                        var decrypted = encryptionService.decrypt(
                                c.getIv(), c.getPanCiphertext());
                        String pan = new String(decrypted, StandardCharsets.UTF_8);
                        return new Dtos.CardResponse(c.getId(), c.getCardholderName(),
                                PanUtils.maskPan(pan), c.getCreatedAt());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

    }
}