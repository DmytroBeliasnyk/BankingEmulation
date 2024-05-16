package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.models.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatesRepository extends JpaRepository<CurrencyRate, Long> {
    Optional<CurrencyRate> findByCurrencyAndDate(String currency, String date);

    Boolean existsByDate(String date);

    void deleteAllByDate(String date);
}
