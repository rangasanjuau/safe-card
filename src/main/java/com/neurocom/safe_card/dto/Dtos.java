package com.neurocom.safe_card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;
import java.util.UUID;

public class Dtos {

    // Request Object
    public record CardRequest(@NotBlank(message = "Cardholder name cannot be blank")
                              String cardholderName,
                              String pan) {
    }

    // Response Object
    public record CardResponse(UUID id, String cardholderName, String maskedPan, Instant creationTime) {
    }
}