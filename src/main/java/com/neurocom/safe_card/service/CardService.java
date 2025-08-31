package com.neurocom.safe_card.service;

import com.neurocom.safe_card.dto.Dtos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class CardService {

    //TODO: 1. Implement Repository

    //TODO: 2. Implement CREATE card method
    @Transactional
    public Dtos.CardResponse create(String name, String pan) throws Exception {
        // TODO; 3. Validate luhn and length (Utility)

        // TODO: 4. Encrypt PAN with AES-256-GCM (Utility)
        // TODO: 5. Calculate HMAC of the PAN for indexing (Utility)
        // TODO: 6. Build the Card object and Store to DB (Repository)
        return new Dtos.CardResponse(null, name, "mask", null);
    }

    //TODO: 7. Implement search card method
    @Transactional(readOnly = true)
    public List<Dtos.CardResponse> searchPan(String rawPan) {

        // TODO: 8. Calculate HMAC (Utility)
        // TODO: 9. Query DB by HMAC (Repository)
        return Collections.emptyList();
    }

}
