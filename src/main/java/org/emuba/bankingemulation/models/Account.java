package org.emuba.bankingemulation.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private CustomClient client;

    private String accountNumber;
    @Enumerated(EnumType.STRING)
    private TypeCurrency currency;

    private double balance;

    private Account(TypeCurrency currency) {
        this.currency = currency;
    }

    public static Account of(TypeCurrency currency) {
        Account Account = new Account(currency);
        Account.setAccountNumber(AccountNumberGenerator.generateUniqueAccountNumber());
        return Account;
    }

    public AccountDTO toDTO() {
        return AccountDTO.of(accountNumber, currency, balance);
    }

    static class AccountNumberGenerator {

        private static final int ACCOUNT_NUMBER_LENGTH = 16;
        private static final Random random = new Random();
        private static final Set<String> generatedNumbers = new HashSet<>();

        public static String generateUniqueAccountNumber() {
            String accountNumber;
            do {
                accountNumber = generateRandomAccountNumber();
            } while (!generatedNumbers.add(accountNumber));
            return accountNumber;
        }

        private static String generateRandomAccountNumber() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
                sb.append(random.nextInt(10));
            }
            return sb.toString();
        }
    }
}
