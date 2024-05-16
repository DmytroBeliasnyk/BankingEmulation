package org.emuba.bankingemulation.services.impl;

import org.emuba.bankingemulation.dto.CurrencyRateDTO;
import org.emuba.bankingemulation.models.CurrencyRate;
import org.emuba.bankingemulation.repositories.RatesRepository;
import org.emuba.bankingemulation.services.RatesService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@EnableScheduling
public class RatesServiceImpl implements RatesService {
    private final RatesRepository ratesRepository;

    public RatesServiceImpl(RatesRepository ratesRepository) {
        this.ratesRepository = ratesRepository;
    }

    @Override
    @Transactional
    public void save(CurrencyRate currencyRate) {
        ratesRepository.save(currencyRate);
    }

    @Override
    @Transactional(readOnly = true)
    public CurrencyRateDTO find(String currency) {
        return ratesRepository.findByCurrency(currency)
                .map(CurrencyRate::toDTO).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(String currency) {
        return ratesRepository.existsByCurrency(currency);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 30 15 * * *", zone = "Europe/Kiev")
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteNonActualRates() {
        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        if (ratesRepository.existsByDate(date))
            ratesRepository.deleteAllByDate(date);
    }
}
