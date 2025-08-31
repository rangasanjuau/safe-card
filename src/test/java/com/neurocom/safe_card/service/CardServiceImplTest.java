package com.neurocom.safe_card.service;

import com.neurocom.safe_card.dto.Dtos;
import com.neurocom.safe_card.entity.Card;
import com.neurocom.safe_card.exception.InvalidPanException;
import com.neurocom.safe_card.repository.CardRepository;
import com.neurocom.safe_card.utils.EncryptionService;
import com.neurocom.safe_card.utils.HmacService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceImplTest {

    public static final UUID RANDOM_UUID = UUID.randomUUID();
    private CardRepository repo;
    private EncryptionService encryptionService;
    private HmacService hmacService;
    private CardServiceImpl service;

    @BeforeEach
    void setup() {
        repo = mock(CardRepository.class);
        encryptionService = mock(EncryptionService.class);
        hmacService = mock(HmacService.class);
        service = new CardServiceImpl(repo, encryptionService, hmacService);
    }

    @Test
    void testCreate_validPan_savesAndReturnsResponse() {
        String name = "Alice";
        String pan = "4556737586899855"; // Luhn valid


        byte[] plaintext = pan.getBytes(StandardCharsets.UTF_8);

        byte[] iv = "iv-bytes-123456".getBytes(StandardCharsets.UTF_8);
        byte[] ciphertext = "cipher".getBytes(StandardCharsets.UTF_8);
        EncryptionService.CipherRecord cipherRecord = new EncryptionService.CipherRecord(iv, ciphertext);

        when(encryptionService.encrypt(plaintext)).thenReturn(cipherRecord);
        when(hmacService.getHmacHex(pan)).thenReturn("hmac-value");
        String idx = hmacService.getHmacHex("9855");
        Card saved = new Card();
        saved.setId(RANDOM_UUID);
        saved.setCardholderName(name);
        saved.setIv(iv);
        saved.setPanCiphertext(ciphertext);
        saved.setPanHmac("hmac-value");
        saved.setLast4Hmac(idx);
        saved.setCreatedAt(Instant.now());

        when(repo.saveAndFlush(any(Card.class))).thenReturn(saved);

        Dtos.CardResponse response = service.create(name, pan);

        assertEquals(RANDOM_UUID, response.id());
        assertEquals(name, response.cardholderName());
        assertTrue(response.maskedPan().startsWith("4556 73"));
        assertTrue(response.maskedPan().endsWith("9855"));

        // Verify repository save was called with correct Card
        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);
        verify(repo).saveAndFlush(cardCaptor.capture());
        Card captured = cardCaptor.getValue();
        assertEquals("hmac-value", captured.getPanHmac());
        assertEquals(idx, captured.getLast4Hmac());
    }

    @Test
    void testCreate_invalidPan_throwsException() {
        String badPan = "123456"; // too short

        assertThrows(InvalidPanException.class, () ->
                service.create("Bob", badPan));
        verifyNoInteractions(repo, encryptionService, hmacService);
    }

    @Test
    void testSearchByPan_returnsMatchingCards() {
        String pan = "4556737586899855";
        String hmac = "hmac-value";

        Card card = new Card();
        card.setId(RANDOM_UUID);
        card.setCardholderName("Charlie");
        card.setPanHmac(hmac);
        card.setCreatedAt(Instant.now());

        when(hmacService.getHmacHex(pan)).thenReturn(hmac);
        when(repo.findByPanHmac(hmac)).thenReturn(List.of(card));

        List<Dtos.CardResponse> result = service.searchByPan(pan);

        assertEquals(1, result.size());
        Dtos.CardResponse dto = result.get(0);
        assertEquals("Charlie", dto.cardholderName());
        assertTrue(dto.maskedPan().endsWith("9855"));
    }

    @Test
    void testSearchByLast4Digits_decryptsAndReturnsResults() {
        String pan = "4556737586899855";
        byte[] iv = "iv".getBytes(StandardCharsets.UTF_8);
        byte[] ct = "ct".getBytes(StandardCharsets.UTF_8);

        Card card = new Card();
        card.setId(RANDOM_UUID);
        card.setCardholderName("Diana");
        card.setIv(iv);
        card.setPanCiphertext(ct);
        card.setCreatedAt(Instant.now());

        String idx = hmacService.getHmacHex("9855");

        when(repo.findByLast4Hmac(idx)).thenReturn(List.of(card));
        when(encryptionService.decrypt(iv, ct)).thenReturn(pan.getBytes(StandardCharsets.UTF_8));

        List<Dtos.CardResponse> results = service.searchByLast4Digits("9855");

        assertEquals(1, results.size());
        Dtos.CardResponse dto = results.get(0);
        assertEquals("Diana", dto.cardholderName());
        assertTrue(dto.maskedPan().startsWith("4556 73"));
        assertTrue(dto.maskedPan().endsWith("9855"));
    }
}
