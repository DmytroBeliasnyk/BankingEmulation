package org.emuba.bankingemulation.services.impl;

import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.repositories.AccountRepository;
import org.emuba.bankingemulation.services.AccountService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final ClientServiceImpl clientService;
    private final AccountNumberGenerator numberGenerator;

    public AccountServiceImpl(AccountRepository accountRepository,
                              ClientServiceImpl clientService,
                              AccountNumberGenerator numberGenerator) {
        this.accountRepository = accountRepository;
        this.clientService = clientService;
        this.numberGenerator = numberGenerator;
    }

    @Override
    @Transactional
    public void addNewAccount(TypeCurrency currency, String login) {
        if (findAccount(currency, login).isPresent())
            return;
        CustomClient client = clientService.findClientByLogin(login);

        Account newAccount = Account.of(numberGenerator.generateUniqueAccountNumber(),
                currency);
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
    public synchronized void updateBalance(Long clientId, TypeCurrency currency, BigDecimal newBalance) {
        accountRepository.updateBalance(clientId, currency, newBalance);
    }
}
