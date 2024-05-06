package org.emuba.bankingemulation.services.impl;

import org.emuba.bankingemulation.dto.CurrencyRateDTO;
import org.emuba.bankingemulation.models.CurrencyRate;
import org.emuba.bankingemulation.repositories.RateRepository;
import org.emuba.bankingemulation.services.RateService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@EnableScheduling
public class RateServiceImpl implements RateService {
    private final RateRepository rateRepository;

    public RateServiceImpl(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    @Override
    @Transactional
    public void save(CurrencyRate rate) {
        rateRepository.save(rate);
    }

    @Override
    @Transactional(readOnly = true)
    public CurrencyRateDTO find(String currency, LocalDate date) {
        String formatted = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return rateRepository.findByCurrencyAndDate(currency, formatted)
                .map(CurrencyRate::toDTO).orElse(null);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 30 15 * * *", zone = "Europe/Kiev")
    public void deleteNonActualRates() {
        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        if (rateRepository.existsByDate(date))
            rateRepository.deleteAllByDate(date);
    }
}
