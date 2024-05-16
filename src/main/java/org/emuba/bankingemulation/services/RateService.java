package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.CurrencyRateDTO;
import org.emuba.bankingemulation.models.CurrencyRate;

import java.time.LocalDate;

public interface RateService {
    void save(CurrencyRate rate);

    CurrencyRateDTO find(String currency);

    void deleteNonActualRates();
}
