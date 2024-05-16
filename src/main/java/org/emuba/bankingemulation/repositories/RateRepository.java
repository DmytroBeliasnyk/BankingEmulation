package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.models.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<CurrencyRate, Long> {
    CurrencyRate findByCurrency(String currency);

    Boolean existsByDate(String date);

    void deleteAllByDate(String date);
}
