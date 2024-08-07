package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.CustomClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCurrencyAndClient_Login(TypeCurrency currency, String login);

    Optional<Account> findByCurrencyAndClient_Id(TypeCurrency currency, Long clientId);

    Optional<Account> findAccountByAccountNumber(String accountNumber);

    List<Account> findAllByClient(CustomClient client);

    Boolean existsByAccountNumber(String accountNumber);
}
