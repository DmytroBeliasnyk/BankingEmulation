package org.emuba.bankingemulation.repository;

import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.model.Account;
import org.emuba.bankingemulation.model.CustomClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCurrencyAndClient_Login(TypeCurrency currency, String login);
}
