package org.emuba.bankingemulation.retrievers;

import org.emuba.bankingemulation.dto.CurrencyRateDTO;
import org.emuba.bankingemulation.models.CurrencyRate;
import org.emuba.bankingemulation.services.impl.RatesServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CurrencyRatesRetriever {
    private static final String baseUrl =
            "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=...&date=________&json";
    private final RatesServiceImpl ratesService;

    public CurrencyRatesRetriever(RatesServiceImpl ratesService) {
        this.ratesService = ratesService;
    }

    public CurrencyRateDTO getRate(String currency, LocalDate date) {
        String url = baseUrl.replace("...", currency)
                .replace("________", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CurrencyRate[]> response = restTemplate.getForEntity(url,
                CurrencyRate[].class);

        CurrencyRate rate = response.getBody()[0];

        ratesService.save(rate);
        return rate.toDTO();
    }
}
