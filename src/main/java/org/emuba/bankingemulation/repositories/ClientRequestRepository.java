package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.enums.ClientRequestType;
import org.emuba.bankingemulation.models.ClientRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRequestRepository extends JpaRepository<ClientRequest, Long> {
    List<ClientRequest> findAllByClientRequestType(ClientRequestType clientRequestType, Pageable pageable);
}
