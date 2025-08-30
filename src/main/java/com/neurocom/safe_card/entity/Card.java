package com.neurocom.safe_card.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity @Table(name = "cards", indexes = {
        @Index(name = "index_pan", columnList = "panHmac", unique = false),
        @Index(name = "index_last4", columnList = "last4Hmac", unique = false)
})
@Getter @Setter
public class Card {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String cardholderName;

    // Encrypted PAN (AES-256-GCM)
    @Lob @Column(nullable = false)
    private byte[] panCiphertext;

    // Initialization Vector for AES-GCM
    @Column(nullable = false, length = 16)
    private byte[] iv;

    // HMAC index for full PAN
    @Column(nullable = false, length = 64)
    private String panHmac;

    // HMAC index for last 4 digits
    @Column(nullable = false, length = 64)
    private String last4Hmac;

    // Creation timestamp
    @CreationTimestamp
    private Instant createdAt;
}