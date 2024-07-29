package org.emuba.bankingemulation.models;

import jakarta.persistence.*;
import lombok.*;
import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
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

    private BigDecimal balance = BigDecimal.ZERO;

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

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", client=" + client.getName() + client.getSurname() +
                ", accountNumber='" + accountNumber + '\'' +
                ", currency=" + currency +
                ", balance=" + balance +
                '}';
    }
}
