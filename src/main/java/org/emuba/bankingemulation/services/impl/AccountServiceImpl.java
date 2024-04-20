package org.emuba.bankingemulation.services.impl;

import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.repositories.AccountRepository;
import org.emuba.bankingemulation.services.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        if (findAccount(currency, login).isPresent())
            return;
        CustomClient client = clientService.findClientByLogin(login);
        Account newAccount = Account.of(currency);
        client.addAccount(newAccount);
        accountRepository.save(newAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Account> findAccount(TypeCurrency currency, String login) {
        return accountRepository.findByCurrencyAndClient_Login(currency, login);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDTO> findAllByClient(CustomClient client) {
        return accountRepository.findAllByClient(client)
                .stream()
                .map(Account::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Account> findAccountByNumber(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber);
    }

    @Override
    @Transactional
    public synchronized void updateBalance(Long clientId, TypeCurrency currency, double newBalance) {
        accountRepository.updateBalance(clientId, currency, newBalance);
    }
}
