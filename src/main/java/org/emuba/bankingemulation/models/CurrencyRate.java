package org.emuba.bankingemulation.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.emuba.bankingemulation.dto.CurrencyRateDTO;

import java.time.LocalDate;

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
