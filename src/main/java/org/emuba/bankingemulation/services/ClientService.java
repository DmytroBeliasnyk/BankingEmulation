package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.ClientDTO;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.models.CustomClient;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {

    void addClient(String name, String surname, String email,
                   String login, String passHash, UserRole role);

    CustomClient findById(Long id);

    List<ClientDTO> findAllClients(Pageable pageable);

    CustomClient findClientByLogin(String login);

    boolean existsByLogin(String login);

    long countClients();
}
