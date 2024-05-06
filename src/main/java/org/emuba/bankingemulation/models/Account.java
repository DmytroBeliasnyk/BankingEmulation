package org.emuba.bankingemulation.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.services.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

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

    private Account(String accountNumber, TypeCurrency currency) {
        this.accountNumber = accountNumber;
        this.currency = currency;
    }

    public static Account of(String accountNumber, TypeCurrency currency) {
        return new Account(accountNumber, currency);
    }

    public AccountDTO toDTO() {
        return AccountDTO.of(accountNumber, currency, balance);
    }
}
