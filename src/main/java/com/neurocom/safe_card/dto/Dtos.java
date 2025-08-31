package com.neurocom.safe_card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;
import java.util.UUID;

public class Dtos {

    // Request Object
    public record CardRequest(@NotBlank(message = "Cardholder name cannot be blank")
                              String cardholderName,

                              @Pattern(regexp = "\\d{12,19}", message = "PAN must be 12–19 digits")
                              String pan) {
    }

    // Response Object
    public record CardResponse(UUID id, String cardholderName, String maskedPan, Instant creationTime) {
    }
}