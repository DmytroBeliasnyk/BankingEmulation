package org.emuba.bankingemulation.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyRate {
    @JsonProperty("cc")
    private String currency;
    private double rate;
    @JsonProperty("exchangedate")
    private String date;
}
