package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.models.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RateRepository extends JpaRepository<CurrencyRate, Long> {
    Optional<CurrencyRate> findByCurrencyAndDate(String currency, String date);
}
