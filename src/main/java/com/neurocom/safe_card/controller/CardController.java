package com.neurocom.safe_card.controller;


import com.neurocom.safe_card.dto.Dtos;
import com.neurocom.safe_card.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {


    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


    @PostMapping
    public ResponseEntity<Dtos.CardResponse> create(@RequestBody @Valid Dtos.CardRequest
                                                            req) throws Exception {

        return ResponseEntity.ok(cardService.create(req.cardholderName(), req.pan()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Dtos.CardResponse>> search(@RequestParam(required = false)
                                                          String pan) {
        if (pan != null) return ResponseEntity.ok(cardService.searchPan(pan));

        return ResponseEntity.badRequest().build();
    }

}
