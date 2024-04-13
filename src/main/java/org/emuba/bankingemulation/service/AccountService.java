package org.emuba.bankingemulation.service;

import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;

public interface AccountService {
    void addNewAccount(TypeCurrency currency, String login);

    AccountDTO findAccount(TypeCurrency currency, String login);
}
