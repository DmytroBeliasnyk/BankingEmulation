package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.TransactionDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.CustomClient;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface HistoryService {
    void saveTransaction(CustomClient fromClient, String fromAccountNumber, TypeCurrency fromCurrency,
                         CustomClient toClient, String toAccountNumber, TypeCurrency toCurrency,
                         LocalDate date, BigDecimal amount);

    TransactionDTO find(Long id);

    List<TransactionDTO> findByClientLogin(String login, Pageable pageable);

    List<TransactionDTO> findAllByDate(String login, LocalDate date, Pageable pageable);

    List<TransactionDTO> findAllBetweenDate(String login,
                                            LocalDate startDate, LocalDate endDate,
                                            Pageable pageable);

    long count();
}
