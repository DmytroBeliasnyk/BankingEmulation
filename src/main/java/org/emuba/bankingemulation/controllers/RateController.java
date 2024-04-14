package org.emuba.bankingemulation.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.emuba.bankingemulation.dto.CurrencyRateDTO;
import org.emuba.bankingemulation.models.CurrencyRate;
import org.emuba.bankingemulation.retrievers.CurrencyRatesRetriever;
import org.emuba.bankingemulation.services.impl.RateServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@RestController
public class RateController {
    private final CurrencyRatesRetriever retriever;
    private final RateServiceImpl rateService;

    public RateController(CurrencyRatesRetriever retriever, RateServiceImpl rateService) {
        this.retriever = retriever;
        this.rateService = rateService;
    }

    @GetMapping("/rate")
    public ResponseEntity<CurrencyRateDTO> getRate(@RequestParam String currency) {
        LocalDate date = LocalDate.now();
        CurrencyRateDTO rate = rateService.find(currency, date);
        if (rate != null)
            return new ResponseEntity<>(rate, HttpStatus.OK);

        return new ResponseEntity<>(retriever.getRate(currency, date),
                HttpStatus.OK);
    }
}
