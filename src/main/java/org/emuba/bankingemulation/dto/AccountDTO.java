package org.emuba.bankingemulation.dto;

import lombok.Data;
import org.emuba.bankingemulation.enums.TypeCurrency;

@Data
public class AccountDTO {
    private String accountNumber;
    private TypeCurrency currency;
    private double balance;

    private AccountDTO(String accountNumber, TypeCurrency currency, double balance) {
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = balance;
    }

    public static AccountDTO of(String accountNumber, TypeCurrency currency, double balance) {
        return new AccountDTO(accountNumber, currency, balance);
    }
}
