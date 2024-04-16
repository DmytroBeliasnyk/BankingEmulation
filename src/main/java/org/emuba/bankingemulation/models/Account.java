package org.emuba.bankingemulation.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;

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
}
