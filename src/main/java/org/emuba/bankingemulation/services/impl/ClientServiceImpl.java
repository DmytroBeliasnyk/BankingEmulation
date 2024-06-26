package org.emuba.bankingemulation.services.impl;

import org.emuba.bankingemulation.dto.ClientDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.repositories.ClientRepository;
import org.emuba.bankingemulation.services.ClientService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final AccountNumberGenerator numberGenerator;

    public ClientServiceImpl(ClientRepository clientRepository,
                             AccountNumberGenerator numberGenerator) {
        this.clientRepository = clientRepository;
        this.numberGenerator = numberGenerator;
    }

    @Override
    @Transactional
    public void addClient(String name, String surname, String email,
                          String login, String passHash, UserRole role) {
        CustomClient client = CustomClient.of(name, surname, email, login, passHash, role);
        if (role != UserRole.ADMIN)
            client.addAccount(Account.of(numberGenerator.generateUniqueAccountNumber(),
                    TypeCurrency.UAH));
        clientRepository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomClient findById(Long id) {
        return clientRepository.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> findAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable).getContent()
                .stream()
                .filter(c -> c.getRole() == UserRole.USER)
                .map(CustomClient::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomClient findClientByLogin(String login) {
        return clientRepository.findByLogin(login);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByLogin(String login) {
        return clientRepository.existsByLogin(login);
    }

    @Override
    @Transactional(readOnly = true)
    public long countClients() {
        return clientRepository.count();
    }
}
