package org.emuba.bankingemulation.controllers;

import org.emuba.bankingemulation.dto.CurrencyRateDTO;
import org.emuba.bankingemulation.retrievers.CurrencyRatesRetriever;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/rate")
public class RateController {
    private final CurrencyRatesRetriever retriever;

    public RateController(CurrencyRatesRetriever retriever) {
        this.retriever = retriever;
    }

    @GetMapping("get")
    public ResponseEntity<CurrencyRateDTO> getRate(@RequestParam String currency) {
        return new ResponseEntity<>(retriever.getRate(currency, LocalDate.now()),
                HttpStatus.OK);
    }
}
