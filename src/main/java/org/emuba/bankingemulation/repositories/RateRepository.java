package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.models.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RateRepository extends JpaRepository<CurrencyRate, Long> {
    Optional<CurrencyRate> findByCurrency(String currency);

    Boolean existsByDate(String date);

    void deleteAllByDate(String date);
}
