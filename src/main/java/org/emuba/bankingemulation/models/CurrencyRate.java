package org.emuba.bankingemulation.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.emuba.bankingemulation.dto.CurrencyRateDTO;

@Entity
@Data
@NoArgsConstructor
public class CurrencyRate {
    @Id
    @GeneratedValue
    private Long id;
    @JsonProperty("cc")
    private String currency;
    private double rate;
    @JsonProperty("exchangedate")
    private String date;

    public CurrencyRateDTO toDTO() {
        return CurrencyRateDTO.of(currency, rate, date);
    }
}
