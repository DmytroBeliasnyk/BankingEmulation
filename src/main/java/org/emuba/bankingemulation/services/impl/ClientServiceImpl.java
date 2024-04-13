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

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public void addClient(String name, String surname, String email,
                          String login, String passHash, UserRole role) {
        CustomClient client = CustomClient.of(name, surname, email, login, passHash, role);
        if (role != UserRole.ADMIN)
            client.addAccount(Account.of(TypeCurrency.UAH));
        clientRepository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> findAllClients(Pageable pageable) {
        List<CustomClient> clients = clientRepository.findAll(pageable).getContent();
        List<ClientDTO> res = new ArrayList<>();

        clients.stream()
                .filter(c -> c.getRole() == UserRole.USER)
                .forEach(c -> res.add(c.toDTO()));
        return res;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomClient findClientByLogin(String login) {
        return clientRepository.findByLogin(login);
    }

    @Override
    @Transactional(readOnly = true)
    public long findClientId(String login) {
        return clientRepository.findByLogin(login).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findAllLogins() {
        return clientRepository.findAllLogins();
    }
}
