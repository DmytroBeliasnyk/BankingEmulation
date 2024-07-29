package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.TransactionDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.models.TransactionHistory;
import org.emuba.bankingemulation.repositories.ClientRepository;
import org.emuba.bankingemulation.repositories.HistoryRepository;
import org.emuba.bankingemulation.services.impl.HistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceTests {
    @InjectMocks
    private HistoryServiceImpl historyService;
    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private ClientRepository clientRepository;
    private TransactionHistory transactionHistory;
    private CustomClient client;

    @BeforeEach
    void init() {
        client = CustomClient.of("Client", "Client",
                "email@email.com", "login", "password", UserRole.USER);
        transactionHistory = TransactionHistory.of(
                client, "123123", TypeCurrency.UAH,
                client, "456456", TypeCurrency.USD,
                LocalDate.now(), BigDecimal.TEN);
    }

    @Test
    void saveTransaction() {
        historyService.saveTransaction(client, "123123", TypeCurrency.UAH,
                client, "456456", TypeCurrency.USD,
                LocalDate.now(), BigDecimal.TEN);

        verify(historyRepository, times(1)).save(transactionHistory);
    }

    @Test
    void find() {
        when(historyRepository.findById(1L)).thenReturn(Optional.of(transactionHistory));

        TransactionDTO transaction = historyService.find(1L);

        assertThat(transaction).isNotNull();
        assertThat(transaction).isEqualTo(transactionHistory.toDTO());
    }

    @Test
    void findByClientLogin() {
        Pageable pageable = PageRequest.of(0, 1);

        when(clientRepository.findByLogin("login")).thenReturn(client);
        when(historyRepository.findByClientId(client, pageable)).thenReturn(List.of(transactionHistory));

        List<TransactionDTO> transactions = historyService.findByClientLogin("login", pageable);

        assertThat(transactions).isNotNull();
        assertThat(transactions).isNotEmpty();
        assertThat(transactions).hasSize(1);
        assertThat(transactions).contains(transactionHistory.toDTO());
    }

    @Test
    void findAllByDate() {
        TransactionHistory transaction1 = TransactionHistory.of(client, "123123", TypeCurrency.UAH,
                client, "456456", TypeCurrency.USD,
                LocalDate.now(), BigDecimal.TEN);
        Pageable pageable = PageRequest.of(0, 2);

        when(clientRepository.findByLogin("login")).thenReturn(client);
        when(historyRepository.findAllByDate(client, LocalDate.now(), pageable))
                .thenReturn(List.of(transactionHistory, transaction1));

        List<TransactionDTO> transactions = historyService.findAllByDate(
                "login", LocalDate.now(), pageable);

        assertThat(transactions).isNotNull();
        assertThat(transactions).isNotEmpty();
        assertThat(transactions).hasSize(2);
        assertThat(transactions).contains(transactionHistory.toDTO(), transaction1.toDTO());
    }

    @Test
    void findAllBetweenDate() {
        TransactionHistory transaction1 = TransactionHistory.of(client, "123123", TypeCurrency.UAH,
                client, "456456", TypeCurrency.USD,
                LocalDate.of(2024, 7, 5), BigDecimal.TEN);
        Pageable pageable = PageRequest.of(0, 2);

        when(clientRepository.findByLogin("login")).thenReturn(client);
        when(historyRepository.findAllBetweenDate(client, LocalDate.of(2024, 7, 5),
                LocalDate.of(2024, 7, 31), pageable))
                .thenReturn(List.of(transactionHistory, transaction1));

        List<TransactionDTO> transactions = historyService.findAllBetweenDate(
                "login", LocalDate.of(2024, 7, 5),
                LocalDate.of(2024, 7, 31), pageable);

        assertThat(transactions).isNotNull();
        assertThat(transactions).isNotEmpty();
        assertThat(transactions).hasSize(2);
        assertThat(transactions).contains(transactionHistory.toDTO(), transaction1.toDTO());
    }

    @Test
    void count() {
        when(historyRepository.count()).thenReturn(5L);

        long clients = historyService.count();

        assertThat(clients).isEqualTo(5);
    }
}
