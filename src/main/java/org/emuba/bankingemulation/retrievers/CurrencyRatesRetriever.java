package org.emuba.bankingemulation.retrievers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.emuba.bankingemulation.json.CurrencyRate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CurrencyRatesRetriever {
    private static final String baseUrl =
            "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=...&date=________&json";

    public CurrencyRate getRate(String currency, String date) throws JsonProcessingException {
        String url = baseUrl.replace("...", currency)
                .replace("________", date);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CurrencyRate[]> response = restTemplate.getForEntity(url,
                CurrencyRate[].class);
        return response.getBody()[0];
    }
}
