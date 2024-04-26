package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.models.CustomClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<CustomClient, Long> {
    CustomClient findByLogin(String login);

    Boolean existsByLogin(String login);
}
