package com.neurocom.safe_card.controller;


import com.neurocom.safe_card.dto.Dtos;
import com.neurocom.safe_card.exception.InvalidRequestException;
import com.neurocom.safe_card.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "*") // allow all origins for testing
public class CardController {


    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


    @PostMapping
    public ResponseEntity<Dtos.CardResponse> create(@RequestBody @Valid Dtos.CardRequest
                                                            req) throws Exception {

        Dtos.CardResponse cardResponse = cardService.create(req.cardholderName(), req.pan());

        return ResponseEntity
                .created(URI.create("/api/cards/" + cardResponse.id())) // Location header
                .body(cardResponse); // Response body
    }

    @GetMapping("/search")
    public ResponseEntity<List<Dtos.CardResponse>> search(@RequestParam(required = false)
                                                          String pan, @RequestParam(required = false)
                                                          String last4) {
        if (pan != null) return ResponseEntity.ok(cardService.searchByPan(pan));
        if (last4 != null) return ResponseEntity.ok(cardService.searchByLast4Digits(last4));

        throw new InvalidRequestException("Either 'pan' or 'last4' query parameter must be provided.");
    }

}
