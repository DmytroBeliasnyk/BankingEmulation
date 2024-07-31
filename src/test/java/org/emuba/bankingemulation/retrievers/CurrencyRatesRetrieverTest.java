package org.emuba.bankingemulation.retrievers;

import org.emuba.bankingemulation.dto.CurrencyRateDTO;
import org.emuba.bankingemulation.models.CurrencyRate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CurrencyRatesRetrieverTest {
    @InjectMocks
    private CurrencyRatesRetriever retriever;

    @Test
    void getRate() {
        CurrencyRate rate = new CurrencyRate();
        rate.setCurrency("USD");
        rate.setRate(41.0478);
        rate.setDate("30.07.2024");

        CurrencyRateDTO currencyRateDTO = retriever.getRate("USD", LocalDate.of(2024, 7, 30));

        assertThat(currencyRateDTO).isNotNull();
        assertThat(currencyRateDTO).isEqualTo(rate.toDTO());
    }
}
