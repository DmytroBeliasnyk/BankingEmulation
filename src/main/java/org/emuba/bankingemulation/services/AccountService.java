package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.Account;

import java.util.Optional;

public interface AccountService {
    void addNewAccount(TypeCurrency currency, String login);

    Optional<Account> findAccount(TypeCurrency currency, String login);

    Optional<Account> findAccountByNumber(String accountNumber);

    void updateBalance(Long clientId, TypeCurrency currency, double newBalance);
}
