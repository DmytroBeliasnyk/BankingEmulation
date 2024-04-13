package org.emuba.bankingemulation.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.emuba.bankingemulation.retrievers.CurrencyRatesRetriever;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@RestController
public class RateController {
    private final CurrencyRatesRetriever retriever;

    public RateController(CurrencyRatesRetriever retriever) {
        this.retriever = retriever;
    }

    @GetMapping("/rate")
    public ResponseEntity<?> getRate(@RequestParam String currency) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdt = new SimpleDateFormat("yyyyMMdd");
        String date = sdt.format(calendar.getTime());
        try {
            return new ResponseEntity<>(retriever.getRate(currency, date),
                    HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
}
