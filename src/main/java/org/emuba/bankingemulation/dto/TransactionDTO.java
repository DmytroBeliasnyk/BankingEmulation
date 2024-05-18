package org.emuba.bankingemulation.dto;

import lombok.Data;
import org.emuba.bankingemulation.enums.TypeCurrency;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class TransactionDTO {
    private Long id;

    private String fromName;
    private String fromAccount;
    private TypeCurrency fromCurrency;

    private String toName;
    private String toAccount;
    private TypeCurrency toCurrency;

    private LocalDateTime date;

    private double amount;

    private TransactionDTO(Long id, String fromName, String fromAccount, TypeCurrency fromCurrency,
                           String toName, String toAccount, TypeCurrency toCurrency,
                           LocalDateTime date, double amount) {
        this.id = id;
        this.fromName = fromName;
        this.fromAccount = fromAccount;
        this.fromCurrency = fromCurrency;
        this.toName = toName;
        this.toAccount = toAccount;
        this.toCurrency = toCurrency;
        this.date = date;
        this.amount = amount;
    }

    public static TransactionDTO of(Long id, String fromName, String fromAccount, TypeCurrency fromCurrency,
                                    String toName, String toAccount, TypeCurrency toCurrency,
                                    LocalDateTime date, double amount) {
        return new TransactionDTO(id, fromName, fromAccount, fromCurrency,
                toName, toAccount, toCurrency, date, amount);
    }

    /*@Override
    public String toString() {
        return "Transaction " + id + " " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                System.lineSeparator() + System.lineSeparator() +
                "From: " + fromName + System.lineSeparator() +
                fromAccount + " " + fromCurrency + System.lineSeparator() + System.lineSeparator() +
                "To: " + toName + System.lineSeparator() +
                toAccount + " " + toCurrency + System.lineSeparator() + System.lineSeparator() +
                amount + " " + fromCurrency;
    }*/
}
