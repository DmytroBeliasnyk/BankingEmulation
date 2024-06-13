package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.models.Credit;
import org.emuba.bankingemulation.models.CustomClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    List<Credit> findAllByClient(CustomClient client);

    @Modifying
    @Query("UPDATE Credit c SET c.amount=:newBalance WHERE c.id=:creditId")
    void updateBalance(@Param("creditId") Long creditId,
                       @Param("newBalance") BigDecimal newBalance);
}
