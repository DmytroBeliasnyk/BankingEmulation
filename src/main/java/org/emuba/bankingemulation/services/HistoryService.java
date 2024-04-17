package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.TransactionDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.CustomClient;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryService {
    void saveTransaction(CustomClient fromClient, String fromAccountNumber, TypeCurrency fromCurrency,
                         CustomClient toClient, String toAccountNumber, TypeCurrency toCurrency,
                         LocalDateTime date, double amount);

    TransactionDTO find(Long id);

    List<TransactionDTO> findByClientLogin(String login, Pageable pageable);

    long count();
}
