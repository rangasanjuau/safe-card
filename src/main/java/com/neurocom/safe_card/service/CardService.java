package com.neurocom.safe_card.service;

import com.neurocom.safe_card.dto.Dtos;

import java.util.List;

public interface CardService {
    public Dtos.CardResponse create(String name, String rawPan) throws Exception;

    public List<Dtos.CardResponse> searchByPan(String rawPan);

    public List<Dtos.CardResponse> searchByLast4Digits(String last4);
}