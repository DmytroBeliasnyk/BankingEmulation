package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.CustomClient;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    void addNewAccount(TypeCurrency currency, String login);

    Optional<Account> findAccount(TypeCurrency currency, String login);

    List<AccountDTO> findAllByClient(CustomClient client);

    Optional<Account> findAccountByNumber(String accountNumber);

    void updateBalance(Long clientId, TypeCurrency currency, double newBalance);
}
