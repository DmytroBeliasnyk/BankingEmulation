package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.ClientDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.repositories.ClientRepository;
import org.emuba.bankingemulation.services.impl.AccountNumberGenerator;
import org.emuba.bankingemulation.services.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {
    @InjectMocks
    private ClientServiceImpl clientService;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private AccountNumberGenerator numberGenerator;
    private CustomClient client;

    @BeforeEach
    void init() {
        client = CustomClient.of("Client", "Client",
                "email@email.com", "login", "password", UserRole.USER);
        client.addAccount(Account.of("123123", TypeCurrency.UAH));
    }

    @Test
    void addClient() {
        when(numberGenerator.generateUniqueAccountNumber()).thenReturn("123123");

        clientService.addClient("Client", "Client",
                "email@email.com", "login", "password", UserRole.USER);

        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void findById() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        CustomClient foundClient = clientService.findById(1L);

        assertThat(foundClient).isNotNull();
        assertThat(foundClient).isEqualTo(client);
    }

    @Test
    void findAllClients() {
        CustomClient client2 = CustomClient.of("Client2", "Client2",
                "email@email.com", "login", "password", UserRole.USER);
        CustomClient admin = CustomClient.of("Admin", "Admin",
                "email@email.com", "login", "password", UserRole.ADMIN);

        Pageable pageable = PageRequest.of(0, 3);
        PageImpl<CustomClient> page = new PageImpl<>(List.of(client, client2, admin));

        when(clientRepository.findAll(pageable)).thenReturn(page);

        List<ClientDTO> clients = clientService.findAllClients(pageable);

        assertThat(clients).isNotNull();
        assertThat(clients).isNotEmpty();
        assertThat(clients).hasSize(2);
        assertThat(clients).contains(client.toDTO(), client2.toDTO());
        assertThat(clients).doesNotContain(admin.toDTO());
    }

    @Test
    void findClientByLogin() {
        when(clientRepository.findByLogin("login")).thenReturn(client);

        CustomClient foundClient = clientService.findClientByLogin("login");

        assertThat(foundClient).isNotNull();
        assertThat(foundClient).isEqualTo(client);
    }

    @Test
    void existByLogin() {
        when(clientRepository.existsByLogin("login")).thenReturn(true);

        Boolean existClient = clientService.existsByLogin("login");

        assertThat(existClient).isNotNull();
        assertThat(existClient).isTrue();
    }

    @Test
    void countClients() {
        when(clientRepository.count()).thenReturn(5L);

        long clients = clientService.countClients();

        assertThat(clients).isEqualTo(5);
    }
}
