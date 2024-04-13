package org.emuba.bankingemulation.repository;

import org.emuba.bankingemulation.model.CustomClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<CustomClient, Long> {
    CustomClient findByLogin(String login);

    @Query("SELECT c.login FROM CustomClient c")
    List<String> findAllLogins();

}
