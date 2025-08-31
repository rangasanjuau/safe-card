package com.neurocom.safe_card.config;


import com.neurocom.safe_card.service.CardService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TestDataLoader implements ApplicationRunner {

    private final CardService cardService;

    public TestDataLoader(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        cardService.create("Alice", "4539578763621486");
        cardService.create("Bob", "4716108999716531");
        cardService.create("Diana", "5425233430109903");
        cardService.create("Charlie", "6011000990139424");
    }
}

