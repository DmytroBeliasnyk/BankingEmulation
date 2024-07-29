package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.models.Credit;
import org.emuba.bankingemulation.models.CustomClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CreditRepositoryTests {
    @Autowired
    private CreditRepository creditRepository;
    @Autowired
    private ClientRepository clientRepository;
    private CustomClient client;

    @BeforeEach
    void init() {
        client = clientRepository.save(CustomClient.of("Client", "Client",
                "email@email.com", "login", "password", UserRole.USER));

        Credit credit = Credit.of(LocalDate.now(), LocalDate.of(2025, 7, 26),
                BigDecimal.TEN, TypeCurrency.USD);
        client.addCredit(credit);

        creditRepository.save(credit);
    }

    @Test
    void findAllByClient() {
        List<Credit> creditList = creditRepository.findAllByClient(client);

        assertThat(creditList).isNotNull();
        assertThat(creditList).isNotEmpty();
        assertThat(creditList).hasSize(1);
    }
}
