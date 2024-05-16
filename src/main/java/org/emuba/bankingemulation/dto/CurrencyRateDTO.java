package org.emuba.bankingemulation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyRateDTO {
    @JsonProperty("cc")
    private String currency;
    private double rate;
    @JsonProperty("exchangedate")
    private String date;
}
