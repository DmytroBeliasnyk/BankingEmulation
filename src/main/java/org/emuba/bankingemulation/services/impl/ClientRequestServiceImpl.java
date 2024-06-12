package org.emuba.bankingemulation.services.impl;

import org.emuba.bankingemulation.dto.ClientRequestDTO;
import org.emuba.bankingemulation.enums.ClientRequestType;
import org.emuba.bankingemulation.models.ClientRequest;
import org.emuba.bankingemulation.repositories.ClientRequestRepository;
import org.emuba.bankingemulation.services.ClientRequestService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientRequestServiceImpl implements ClientRequestService {
    private final ClientRequestRepository clientRequestRepository;
    private final ClientServiceImpl clientService;

    public ClientRequestServiceImpl(ClientRequestRepository clientRequestRepository, ClientServiceImpl clientService) {
        this.clientRequestRepository = clientRequestRepository;
        this.clientService = clientService;
    }

    @Override
    @Transactional
    public void add(String login, ClientRequestType clientRequestType, Long transactionId) {
        clientRequestRepository.save(ClientRequest.of(clientService.findClientByLogin(login),
                clientRequestType, transactionId));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clientRequestRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientRequestDTO> getAll(Pageable pageable) {
        return clientRequestRepository.findAll(pageable)
                .stream()
                .map(ClientRequest::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientRequestDTO> getAllByType(ClientRequestType clientRequestType, Pageable pageable) {
        return clientRequestRepository.findAllByClientRequestType(clientRequestType, pageable)
                .stream()
                .map(ClientRequest::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientRequest find(Long id) {
        return clientRequestRepository.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return clientRequestRepository.count();
    }
}
