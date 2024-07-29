package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.models.Credit;
import org.emuba.bankingemulation.models.CustomClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    List<Credit> findAllByClient(CustomClient client);
}
