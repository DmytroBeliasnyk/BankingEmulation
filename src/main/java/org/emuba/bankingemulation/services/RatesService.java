package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.CurrencyRateDTO;
import org.emuba.bankingemulation.models.CurrencyRate;

public interface RatesService {
    void save(CurrencyRate currencyRate);

    CurrencyRateDTO find(String currency);

    boolean exists(String currency);

    void deleteNonActualRates();
}
