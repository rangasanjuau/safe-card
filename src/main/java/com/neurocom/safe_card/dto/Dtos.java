package com.neurocom.safe_card.dto;

import java.time.Instant;
import java.util.UUID;

public class Dtos {

    // Request Object
    public record CardRequest(String cardholderName, String pan) {
    }

    // Response Object
    public record CardResponse(UUID id, String cardholderName, String maskedPan, Instant creationTime) {
    }
}