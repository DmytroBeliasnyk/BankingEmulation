package org.emuba.bankingemulation.retrievers;

import org.emuba.bankingemulation.dto.CurrencyRateDTO;
import org.emuba.bankingemulation.models.CurrencyRate;
import org.emuba.bankingemulation.services.impl.RateServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class CurrencyRatesRetriever {
    private static final String baseUrl =
            "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=...&date=________&json";
    private final RateServiceImpl rateService;

    public CurrencyRatesRetriever(RateServiceImpl rateService) {
        this.rateService = rateService;
    }

    public CurrencyRateDTO getRate(String currency, LocalDate date) {
        String url = baseUrl.replace("...", currency)
                .replace("________", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CurrencyRate[]> response = restTemplate.getForEntity(url,
                CurrencyRate[].class);
        if (Objects.isNull(Objects.requireNonNull(response.getBody())[0])) {
            return new CurrencyRate().toDTO();
        }
        rateService.save(response.getBody()[0]);
        return response.getBody()[0].toDTO();
    }
}
