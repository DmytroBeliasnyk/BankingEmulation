package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;

public interface AccountService {
    void addNewAccount(TypeCurrency currency, String login);

    AccountDTO findAccount(TypeCurrency currency, String login);

    void updateBalance(Long id, TypeCurrency currency, long newBalance);
}
