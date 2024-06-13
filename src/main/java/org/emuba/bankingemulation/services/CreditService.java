package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.CreditDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CreditService {
    CreditDTO save(String login, LocalDate startDate, LocalDate endDate,
              BigDecimal amount, TypeCurrency currency);

    List<CreditDTO> findAllCredits(String login);

    void updateCreditBalance(Long creditId, BigDecimal amount);

    void updateCreditBalance();

    void deleteCredit(Long creditId);
}
