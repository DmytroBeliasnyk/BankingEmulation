package org.emuba.bankingemulation.service;

import org.emuba.bankingemulation.dto.ClientDTO;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.model.CustomClient;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {

    void addClient(String name, String surname, String email,
                   String login, String passHash, UserRole role);

    List<ClientDTO> findAllClients(Pageable pageable);

    CustomClient findClientByLogin(String login);

    long findClientId(String login);

    List<String> findAllLogins();
}
