package org.emuba.bankingemulation.dto;

import lombok.Data;
import org.emuba.bankingemulation.enums.TypeCurrency;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreditDTO {
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal amount;
    private TypeCurrency currency;

    private BigDecimal onePayment;

    private CreditDTO(Long id, LocalDate startDate, LocalDate endDate, BigDecimal amount,
                      TypeCurrency currency, BigDecimal onePayment) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.currency = currency;
        this.onePayment = onePayment;
    }

    public static CreditDTO of(Long id, LocalDate startDate, LocalDate endDate, BigDecimal amount,
                               TypeCurrency currency, BigDecimal onePayment) {
        return new CreditDTO(id, startDate, endDate, amount, currency, onePayment);
    }

    @Override
    public String toString() {
        return "Your credit " + id + System.lineSeparator() +
                "From date " + startDate + " to " + endDate + System.lineSeparator() +
                "Amount " + amount + currency + System.lineSeparator() +
                "One payment " + onePayment + currency;
    }
}
