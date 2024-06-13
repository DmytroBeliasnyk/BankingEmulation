package org.emuba.bankingemulation.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.emuba.bankingemulation.dto.CreditDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class Credit {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private CustomClient client;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal amount;
    private TypeCurrency currency;

    private BigDecimal onePayment;

    private Credit(LocalDate startDate, LocalDate endDate, BigDecimal amount, TypeCurrency currency) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.currency = currency;
        onePayment = amount.divide(new BigDecimal(startDate.until(endDate).getMonths()));
    }

    public static Credit of(LocalDate startDate, LocalDate endDate, BigDecimal amount, TypeCurrency currency) {
        return new Credit(startDate, endDate, amount, currency);
    }

    public CreditDTO toDTO() {
        return CreditDTO.of(id, startDate, endDate, amount, currency, onePayment);
    }
}
