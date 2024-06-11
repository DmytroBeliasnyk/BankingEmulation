package org.emuba.bankingemulation.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.emuba.bankingemulation.dto.TransactionDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class TransactionHistory {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fromClient_id")
    private CustomClient fromClient;
    private String fromAccountNumber;
    private TypeCurrency fromCurrency;

    @ManyToOne
    @JoinColumn(name = "toClient_id")
    private CustomClient toClient;
    private String toAccountNumber;
    private TypeCurrency toCurrency;

    private LocalDateTime date;

    private BigDecimal amount;

    private TransactionHistory(CustomClient fromClient, String fromAccountNumber, TypeCurrency fromCurrency,
                               CustomClient toClient, String toAccountNumber, TypeCurrency toCurrency,
                               LocalDateTime date, BigDecimal amount) {
        this.fromClient = fromClient;
        this.fromAccountNumber = fromAccountNumber;
        this.fromCurrency = fromCurrency;
        this.toClient = toClient;
        this.toAccountNumber = toAccountNumber;
        this.toCurrency = toCurrency;
        this.date = date;
        this.amount = amount;
    }

    public static TransactionHistory of(CustomClient fromClient, String fromAccountNumber, TypeCurrency fromCurrency,
                                        CustomClient toClient, String toAccountNumber, TypeCurrency toCurrency,
                                        LocalDateTime date, BigDecimal amount) {
        return new TransactionHistory(fromClient, fromAccountNumber, fromCurrency,
                toClient, toAccountNumber, toCurrency, date, amount);
    }

    public TransactionDTO toDTO() {
        return TransactionDTO.of(id, fromClient.getName() + " " + fromClient.getSurname(),
                fromAccountNumber, fromCurrency,
                toClient.getName() + " " + toClient.getSurname(), toAccountNumber, toCurrency,
                date, amount);
    }
}
