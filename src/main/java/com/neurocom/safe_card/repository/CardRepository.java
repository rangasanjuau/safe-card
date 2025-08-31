package com.neurocom.safe_card.repository;

import com.neurocom.safe_card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
    List<Card> findByPanHmac(String panHmac);
    List<Card> findByLast4Hmac(String last4Hmac);
}