package com.neurocom.safe_card.controller;


import com.neurocom.safe_card.dto.Dtos;
import com.neurocom.safe_card.exception.InvalidRequestException;
import com.neurocom.safe_card.service.CardService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/V1/cards")
public class CardController {

    private static final Logger log = LoggerFactory.getLogger(CardController.class);

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<Dtos.CardResponse> create(@RequestBody @Valid Dtos.CardRequest
                                                            req) {
        log.info("Received create card request for cardholder: {}", req.cardholderName());

        Dtos.CardResponse cardResponse = cardService.create(req.cardholderName(), req.pan());

        log.info("Card created with ID: {}", cardResponse.id());

        return ResponseEntity
                .created(URI.create("/api/cards/" + cardResponse.id())) // Location header
                .body(cardResponse); // Response body
    }

    @GetMapping("/search")
    public ResponseEntity<List<Dtos.CardResponse>> search(@RequestParam(required = false)
                                                          String pan, @RequestParam(required = false)
                                                          String last4) {

        log.info("Received search request");
        if (pan != null) return ResponseEntity.ok(cardService.searchByPan(pan));
        if (last4 != null) return ResponseEntity.ok(cardService.searchByLast4Digits(last4));

        throw new InvalidRequestException("Either 'pan' or 'last4' query parameter must be provided.");
    }

}
