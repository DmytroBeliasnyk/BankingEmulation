package org.emuba.bankingemulation.services.impl;

import org.emuba.bankingemulation.dto.TransactionDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.models.TransactionHistory;
import org.emuba.bankingemulation.repositories.ClientRepository;
import org.emuba.bankingemulation.repositories.HistoryRepository;
import org.emuba.bankingemulation.services.HistoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final ClientRepository clientRepository;

    public HistoryServiceImpl(HistoryRepository historyRepository,
                              ClientRepository clientRepository) {
        this.historyRepository = historyRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public void saveTransaction(CustomClient fromClient, String fromAccountNumber,
                                TypeCurrency fromCurrency, CustomClient toClient,
                                String toAccountNumber, TypeCurrency toCurrency,
                                LocalDate date, BigDecimal amount) {
        historyRepository.save(TransactionHistory.of(fromClient, fromAccountNumber, fromCurrency,
                toClient, toAccountNumber, toCurrency, date, amount));
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDTO find(Long id) {
        return historyRepository.findById(id).get().toDTO();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> findByClientLogin(String login, Pageable pageable) {
        return historyRepository.findByClientId(clientRepository.findByLogin(login), pageable)
                .stream()
                .map(TransactionHistory::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> findAllByDate(String login, LocalDate date, Pageable pageable) {
        return historyRepository.findAllByDate(
                        clientRepository.findByLogin(login), date, pageable)
                .stream()
                .map(TransactionHistory::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> findAllBetweenDate(String login,
                                                   LocalDate startDate, LocalDate endDate,
                                                   Pageable pageable) {
        return historyRepository.findAllBetweenDate(
                        clientRepository.findByLogin(login), startDate, endDate, pageable)
                .stream()
                .map(TransactionHistory::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return historyRepository.count();
    }
}
