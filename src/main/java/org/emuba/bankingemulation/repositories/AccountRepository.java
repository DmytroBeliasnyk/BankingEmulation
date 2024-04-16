package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCurrencyAndClient_Login(TypeCurrency currency, String login);

    Optional<Account> findAccountByAccountNumber(String accountNumber);

    @Modifying
    @Query("UPDATE Account a SET a.balance = :newBalance WHERE a.client.id = :clientId AND a.currency = :currency")
    void updateBalance(Long clientId, TypeCurrency currency, double newBalance);

}
