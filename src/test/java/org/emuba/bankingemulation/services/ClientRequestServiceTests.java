package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.ClientRequestDTO;
import org.emuba.bankingemulation.enums.ClientRequestType;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.models.ClientRequest;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.repositories.ClientRepository;
import org.emuba.bankingemulation.repositories.ClientRequestRepository;
import org.emuba.bankingemulation.services.impl.ClientRequestServiceImpl;
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
public class ClientRequestServiceTests {
    @InjectMocks
    private ClientRequestServiceImpl clientRequestService;
    @Mock
    private ClientRequestRepository clientRequestRepository;
    @Mock
    private ClientRepository clientRepository;
    private ClientRequest clientRequest;

    @BeforeEach
    void init() {
        clientRequest = ClientRequest.of(CustomClient.of("Client", "Client",
                        "email@email.com", "login", "password", UserRole.USER),
                ClientRequestType.TRANSACTION_CONFIRMATION, 1L);
    }

    @Test
    void add() {
        CustomClient client = CustomClient.of("Client", "Client",
                "email@email.com", "login", "password", UserRole.USER);
        when(clientRepository.findByLogin("login")).thenReturn(client);

        clientRequestService.add("login",
                ClientRequestType.TRANSACTION_CONFIRMATION, 1L);

        verify(clientRequestRepository, times(1)).save(clientRequest);
    }

    @Test
    void delete() {
        clientRequestService.delete(1L);
        verify(clientRequestRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAll() {
        ClientRequest clientRequest2 = ClientRequest.of(CustomClient.of("Client", "Client",
                        "email@email.com", "login", "password", UserRole.USER),
                ClientRequestType.TRANSACTION_CONFIRMATION, 1L);
        Pageable pageable = PageRequest.of(0, 2);
        PageImpl<ClientRequest> page = new PageImpl<>(List.of(clientRequest, clientRequest2));

        when(clientRequestRepository.findAll(pageable)).thenReturn(page);

        List<ClientRequestDTO> requests = clientRequestService.getAll(pageable);

        assertThat(requests).isNotNull();
        assertThat(requests).isNotEmpty();
        assertThat(requests).hasSize(2);
        assertThat(requests).contains(clientRequest.toDTO(), clientRequest2.toDTO());
    }

    @Test
    void getAllByType() {
        ClientRequest clientRequest2 = ClientRequest.of(CustomClient.of("Client", "Client",
                        "email@email.com", "login", "password", UserRole.USER),
                ClientRequestType.ACCOUNT_BALANCE, 1L);
        Pageable pageable = PageRequest.of(0, 2);

        when(clientRequestRepository.findAllByClientRequestType(
                ClientRequestType.TRANSACTION_CONFIRMATION, pageable))
                .thenReturn(List.of(clientRequest));

        List<ClientRequestDTO> requests = clientRequestService.getAllByType(
                ClientRequestType.TRANSACTION_CONFIRMATION, pageable);

        assertThat(requests).isNotNull();
        assertThat(requests).isNotEmpty();
        assertThat(requests).hasSize(1);
        assertThat(requests).contains(clientRequest.toDTO());
    }

    @Test
    void find() {
        when(clientRequestRepository.findById(1L)).thenReturn(Optional.of(clientRequest));

        ClientRequest fountRequest = clientRequestService.find(1L);

        assertThat(fountRequest).isNotNull();
        assertThat(fountRequest).isEqualTo(clientRequest);
    }

    @Test
    void count() {
        when(clientRequestRepository.count()).thenReturn(5L);

        long requests = clientRequestService.count();

        assertThat(requests).isEqualTo(5);
    }
}
