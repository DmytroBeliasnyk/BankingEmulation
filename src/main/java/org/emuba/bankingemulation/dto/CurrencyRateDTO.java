package org.emuba.bankingemulation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CurrencyRateDTO {
    private String currency;
    private double rate;
    private String date;

    private CurrencyRateDTO(String currency, double rate, String date) {
        this.currency = currency;
        this.rate = rate;
        this.date = date;
    }

    public static CurrencyRateDTO of(String currency, double rate, String date) {
        return new CurrencyRateDTO(currency, rate, date);
    }
}
