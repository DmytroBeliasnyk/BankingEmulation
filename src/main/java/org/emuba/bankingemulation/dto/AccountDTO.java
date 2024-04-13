package org.emuba.bankingemulation.dto;

import lombok.Data;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.model.Account;

@Data
public class AccountDTO {
    private String accountNumber;
    private TypeCurrency currency;
    private double amountMoney;

    private AccountDTO(String accountNumber, TypeCurrency currency, double amountMoney) {
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.amountMoney = amountMoney;
    }

    public static AccountDTO of(String accountNumber, TypeCurrency currency, double amountMoney) {
        return new AccountDTO(accountNumber, currency, amountMoney);
    }
}
