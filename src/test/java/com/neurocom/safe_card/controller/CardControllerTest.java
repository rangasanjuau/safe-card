package com.neurocom.safe_card.controller;

import com.neurocom.safe_card.dto.Dtos;
import com.neurocom.safe_card.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_ReturnsOkResponse() throws Exception {
        // Arrange
        Dtos.CardRequest request = new Dtos.CardRequest("John Doe", "4385123443214342");
        Dtos.CardResponse response = new Dtos.CardResponse(UUID.randomUUID(), "John Doe", "4385 **** 4342", Instant.parse("2025-08-30T10:15:30.00Z"));

        when(cardService.create(request.cardholderName(), request.pan())).thenReturn(response);

        // Act
        ResponseEntity<Dtos.CardResponse> result = cardController.create(request);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(cardService, times(1)).create(request.cardholderName(), request.pan());
    }

    @Test
    void testSearch_WithPan_ReturnsOkResponse() {
        // Arrange
        String pan = "4385123443214342";
        List<Dtos.CardResponse> responses = List.of(
                new Dtos.CardResponse(UUID.randomUUID(), "John Doe", "4385 **** 4342", Instant.parse("2025-08-30T10:15:30.00Z")));

        when(cardService.searchByPan(pan)).thenReturn(responses);

        // Act
        ResponseEntity<List<Dtos.CardResponse>> result = cardController.search(pan, null);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responses, result.getBody());

        verify(cardService, times(1)).searchByPan(pan);
    }

    @Test
    void testSearch_WithLast4DigitsPan_ReturnsOkResponse() {
        // Arrange
        String last4Digits = "4342";
        List<Dtos.CardResponse> responses = List.of(
                new Dtos.CardResponse(UUID.randomUUID(), "John Doe", "4385 **** 4342", Instant.parse("2025-08-30T10:15:30.00Z")));

        when(cardService.searchByLast4Digits(last4Digits)).thenReturn(responses);

        // Act
        ResponseEntity<List<Dtos.CardResponse>> result = cardController.search(null, last4Digits);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responses, result.getBody());

        verify(cardService, times(1)).searchByLast4Digits(last4Digits);
    }

    @Test
    void testSearch_WithNullPanNullLast4Digits_ReturnsBadRequest() {
        // Act
        ResponseEntity<List<Dtos.CardResponse>> result = cardController.search(null, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());

        verify(cardService, never()).searchByPan(any());
        verify(cardService, never()).searchByLast4Digits(any());
    }

}

