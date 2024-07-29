package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.models.CustomClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ClientRepositoryTests {
    @Autowired
    private ClientRepository clientRepository;
    private CustomClient client;

    @BeforeEach
    void init() {
        client = clientRepository.save(CustomClient.of("Client", "Client",
                "email@email.com", "login", "password", UserRole.USER));
    }

    @Test
    void findByLogin() {
        CustomClient foundClient = clientRepository.findByLogin(client.getLogin());

        assertThat(foundClient).isNotNull();
        assertThat(foundClient.getId()).isGreaterThan(0L);
        assertThat(foundClient).isEqualTo(client);
    }

    @Test
    void existByLogin() {
        Boolean existClient = clientRepository.existsByLogin(client.getLogin());

        assertThat(existClient).isNotNull();
        assertThat(existClient).isTrue();
    }
}
