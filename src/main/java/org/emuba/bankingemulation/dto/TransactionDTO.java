package org.emuba.bankingemulation.dto;

import lombok.Data;
import org.emuba.bankingemulation.enums.TypeCurrency;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    private LocalDate date;

    private BigDecimal amount;

    private TransactionDTO(Long id, String fromName, String fromAccount, TypeCurrency fromCurrency,
                           String toName, String toAccount, TypeCurrency toCurrency,
                           LocalDate date, BigDecimal amount) {
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
                                    LocalDate date, BigDecimal amount) {
        return new TransactionDTO(id, fromName, fromAccount, fromCurrency,
                toName, toAccount, toCurrency, date, amount);
    }

    @Override
    public String toString() {
        return "Transaction " + id + " " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                System.lineSeparator() + System.lineSeparator() +
                "From: " + fromName + System.lineSeparator() +
                fromAccount + " " + fromCurrency + System.lineSeparator() + System.lineSeparator() +
                "To: " + toName + System.lineSeparator() +
                toAccount + " " + toCurrency + System.lineSeparator() + System.lineSeparator() +
                amount + " " + fromCurrency;
    }
}
