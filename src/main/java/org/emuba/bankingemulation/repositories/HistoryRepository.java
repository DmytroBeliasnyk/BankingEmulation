package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.models.TransactionHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepository extends JpaRepository<TransactionHistory, Long> {
    @Query("SELECT t FROM TransactionHistory t WHERE t.fromClient = :id OR t.toClient = :id")
    List<TransactionHistory> findByClientId(@Param("id") CustomClient client, Pageable pageable);
}
