package org.emuba.bankingemulation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrencyRateDTO {
    private Long id;
    @JsonProperty("cc")
    private String currency;
    private double rate;
    @JsonProperty("exchangedate")
    private String date;
}
