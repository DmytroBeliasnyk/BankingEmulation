package org.emuba.bankingemulation.service;

import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.model.Account;
import org.emuba.bankingemulation.model.CustomClient;
import org.emuba.bankingemulation.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final ClientServiceImpl clientService;

    public AccountServiceImpl(AccountRepository accountRepository,
                              ClientServiceImpl clientService) {
        this.accountRepository = accountRepository;
        this.clientService = clientService;
    }

    @Override
    @Transactional
    public void addNewAccount(TypeCurrency currency, String login) {
        if (findAccount(currency, login) != null)
            return;
        CustomClient client = clientService.findClientByLogin(login);
        Account newAccount = Account.of(currency);
        client.addAccount(newAccount);
        accountRepository.save(newAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDTO findAccount(TypeCurrency currency, String login) {
        Optional<Account> opt = accountRepository.
                findByCurrencyAndClient_Login(currency, login);
        return opt.map(Account::toDTO).orElse(null);
    }
}
