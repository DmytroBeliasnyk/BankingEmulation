package org.emuba.bankingemulation.services.impl;

import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.retrievers.CurrencyRatesRetriever;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class SupportingMethodsForControllers {
    private final CurrencyRatesRetriever retriever;

    public SupportingMethodsForControllers(CurrencyRatesRetriever retriever) {
        this.retriever = retriever;
    }

    public BigDecimal converter(String fromCurrency, String toCurrency, BigDecimal amount) {
        double rateFrom = 1;
        double rateTo = 1;

        if (!fromCurrency.equalsIgnoreCase("UAH")) {
            rateFrom = retriever.getRate(fromCurrency, LocalDate.now())
                    .getRate();
        }
        if (!toCurrency.equalsIgnoreCase("UAH")) {
            rateTo = retriever.getRate(toCurrency, LocalDate.now())
                    .getRate();
        }

        return amount.multiply(new BigDecimal(rateFrom / rateTo));
    }
    public User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
    public boolean check(String currency) {
        if (currency == null)
            return true;

        try {
            TypeCurrency.valueOf(currency.toUpperCase());
        } catch (IllegalArgumentException e) {
            return true;
        }

        return false;
    }
}
