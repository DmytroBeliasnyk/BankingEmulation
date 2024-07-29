package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.models.TransactionHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class HistoryRepositoryTests {
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private ClientRepository clientRepository;
    private CustomClient client;

    @BeforeEach
    void init() {
        client = clientRepository.save(CustomClient.of("Client", "Client",
                "email@email.com", "login", "password", UserRole.USER));

        historyRepository.save(TransactionHistory.of(client, "123123", TypeCurrency.UAH,
                client, "456456", TypeCurrency.USD,
                LocalDate.now(), BigDecimal.TEN));
        historyRepository.save(TransactionHistory.of(client, "123123", TypeCurrency.UAH,
                client, "456456", TypeCurrency.USD,
                LocalDate.now(), BigDecimal.TEN));
        historyRepository.save(TransactionHistory.of(client, "123123", TypeCurrency.UAH,
                client, "456456", TypeCurrency.USD,
                LocalDate.of(2024, 7, 30), BigDecimal.TEN));
        historyRepository.save(TransactionHistory.of(client, "123123", TypeCurrency.UAH,
                client, "456456", TypeCurrency.USD,
                LocalDate.of(2024, 8, 5), BigDecimal.TEN));

    }

    @Test
    void findByClientId() {
        List<TransactionHistory> transactionHistoryList = historyRepository.findByClientId(
                client, null);

        assertThat(transactionHistoryList).isNotNull();
        assertThat(transactionHistoryList).isNotEmpty();
        assertThat(transactionHistoryList).hasSize(4);
    }

    @Test
    void findByClientId_withPagination() {
        List<TransactionHistory> transactionHistoryList = historyRepository.findByClientId(
                client, PageRequest.of(0, 3));

        assertThat(transactionHistoryList).isNotNull();
        assertThat(transactionHistoryList).isNotEmpty();
        assertThat(transactionHistoryList).hasSize(3);
    }

    @Test
    void findAllByDate() {
        List<TransactionHistory> transactionHistoryList = historyRepository.findAllByDate(
                client, LocalDate.now(), null);

        assertThat(transactionHistoryList).isNotNull();
        assertThat(transactionHistoryList).isNotEmpty();
        assertThat(transactionHistoryList).hasSize(2);
    }

    @Test
    void findAllBetweenDate() {
        List<TransactionHistory> transactionHistoryList = historyRepository.findAllBetweenDate(
                client, LocalDate.of(2024, 7, 1),
                LocalDate.of(2024, 7, 31), null);

        assertThat(transactionHistoryList).isNotNull();
        assertThat(transactionHistoryList).isNotEmpty();
        assertThat(transactionHistoryList).hasSize(3);

    }
}
