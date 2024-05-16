package org.emuba.bankingemulation.retrievers;

import org.emuba.bankingemulation.dto.CurrencyRateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CurrencyRatesRetriever {
    private static final String baseUrl =
            "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=...&date=________&json";

    public CurrencyRateDTO getRate(String currency, LocalDate date) {
        String url = baseUrl.replace("...", currency)
                .replace("________", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CurrencyRateDTO[]> response = restTemplate.getForEntity(
                url, CurrencyRateDTO[].class);

        return response.getBody()[0];
    }
}
