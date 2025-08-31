package com.neurocom.safe_card.service;

import com.neurocom.safe_card.dto.Dtos;
import com.neurocom.safe_card.exception.EncryptionException;
import com.neurocom.safe_card.exception.InvalidPanException;

import java.util.List;

public interface CardService {
    public Dtos.CardResponse create(String name, String rawPan) throws InvalidPanException, EncryptionException;

    public List<Dtos.CardResponse> searchByPan(String rawPan)throws InvalidPanException;

    public List<Dtos.CardResponse> searchByLast4Digits(String last4)throws InvalidPanException;
}