package org.emuba.bankingemulation.services.impl;

import org.emuba.bankingemulation.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountNumberGenerator {
    private static final Random rnd = new Random();
    private final AccountRepository accountRepository;

    public AccountNumberGenerator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = generateRandomAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    private String generateRandomAccountNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }
}
