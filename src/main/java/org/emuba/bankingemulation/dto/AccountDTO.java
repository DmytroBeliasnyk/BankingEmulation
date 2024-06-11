package org.emuba.bankingemulation.dto;

import lombok.Data;
import org.emuba.bankingemulation.enums.TypeCurrency;

import java.math.BigDecimal;

@Data
public class AccountDTO {
    private String accountNumber;
    private TypeCurrency currency;
    private BigDecimal balance;

    private AccountDTO(String accountNumber, TypeCurrency currency, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = balance;
    }

    public static AccountDTO of(String accountNumber, TypeCurrency currency, BigDecimal balance) {
        return new AccountDTO(accountNumber, currency, balance);
    }

    @Override
    public String toString() {
        return "Your account " + accountNumber + System.lineSeparator() +
                "balance: " + balance + " " + currency + System.lineSeparator();
    }
}
