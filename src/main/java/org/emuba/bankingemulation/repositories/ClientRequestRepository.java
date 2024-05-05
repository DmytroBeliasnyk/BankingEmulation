package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.models.ClientRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRequestRepository extends JpaRepository<ClientRequest, Long> {
}
