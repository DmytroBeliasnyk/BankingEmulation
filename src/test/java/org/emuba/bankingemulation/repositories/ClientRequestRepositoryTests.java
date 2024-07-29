package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.enums.ClientRequestType;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.models.ClientRequest;
import org.emuba.bankingemulation.models.CustomClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ClientRequestRepositoryTests {
    @Autowired
    private ClientRequestRepository clientRequestRepository;
    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void init() {
        CustomClient client = CustomClient.of("Client", "Client",
                "email@email.com", "login", "password", UserRole.USER);
        clientRepository.save(client);

        clientRequestRepository.save(ClientRequest.of(client, ClientRequestType.TRANSACTION_CONFIRMATION, null));
        clientRequestRepository.save(ClientRequest.of(client, ClientRequestType.TRANSACTION_CONFIRMATION, null));
        clientRequestRepository.save(ClientRequest.of(client, ClientRequestType.TRANSACTION_CONFIRMATION, null));
        clientRequestRepository.save(ClientRequest.of(client, ClientRequestType.TRANSACTION_CONFIRMATION, null));
        clientRequestRepository.save(ClientRequest.of(client, ClientRequestType.ACCOUNT_BALANCE, null));
    }

    @Test
    void findAllByClientRequestType() {
        List<ClientRequest> requestList = clientRequestRepository.findAllByClientRequestType(
                ClientRequestType.TRANSACTION_CONFIRMATION, null);

        assertThat(requestList).isNotNull();
        assertThat(requestList).isNotEmpty();
        assertThat(requestList).hasSize(4);
    }

    @Test
    void findAllByClientRequestType_withPagination() {
        List<ClientRequest> requestList = clientRequestRepository.findAllByClientRequestType(
                ClientRequestType.TRANSACTION_CONFIRMATION,
                PageRequest.of(0, 2));

        assertThat(requestList).isNotNull();
        assertThat(requestList).isNotEmpty();
        assertThat(requestList).hasSize(2);
    }
}
