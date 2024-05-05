package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.ClientRequestDTO;
import org.emuba.bankingemulation.enums.ClientRequestType;
import org.emuba.bankingemulation.models.ClientRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientRequestService {
    void add(String login, ClientRequestType clientRequestType, Long transactionId);

    void delete(Long id);

    List<ClientRequestDTO> getAll(Pageable pageable);

    ClientRequest find(Long id);

    long count();
}
