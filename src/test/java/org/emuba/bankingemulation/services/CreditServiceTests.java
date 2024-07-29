package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.CreditDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.Credit;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.repositories.AccountRepository;
import org.emuba.bankingemulation.repositories.ClientRepository;
import org.emuba.bankingemulation.repositories.CreditRepository;
import org.emuba.bankingemulation.services.impl.AccountServiceImpl;
import org.emuba.bankingemulation.services.impl.CreditServiceImpl;
import org.emuba.bankingemulation.services.impl.MailService;
import org.emuba.bankingemulation.services.impl.SupportingMethodsForControllers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTests {
    @InjectMocks
    private CreditServiceImpl creditService;
    @Mock
    private CreditRepository creditRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private AccountServiceImpl accountService;
    @Mock
    private SupportingMethodsForControllers supMethods;
    @Mock
    private MailService mailService;
    @Mock
    private AccountRepository accountRepository;
    private CustomClient client;
    private Credit credit;

    @BeforeEach
    void init() {
        client = CustomClient.of("Client", "Client",
                "email@email.com", "login", "password", UserRole.USER);
        credit = Credit.of(LocalDate.now(), LocalDate.of(2025, 7, 29),
                BigDecimal.TEN, TypeCurrency.USD);

        client.addCredit(credit);
    }

    @Test
    void save() {
        when(clientRepository.findByLogin("login")).thenReturn(client);

        CreditDTO creditDTO = creditService.save("login", LocalDate.now(),
                LocalDate.of(2025, 7, 29),
                BigDecimal.TEN, TypeCurrency.USD);

        assertThat(creditDTO).isNotNull();
        assertThat(creditDTO).isEqualTo(credit.toDTO());
        verify(creditRepository, times(1)).save(credit);
    }

    @Test
    void findAllCredits() {
        when(clientRepository.findByLogin("login")).thenReturn(client);
        when(creditRepository.findAllByClient(client)).thenReturn(List.of(credit));

        List<CreditDTO> creditList = creditService.findAllCredits("login");

        assertThat(creditList).isNotNull();
        assertThat(creditList).isNotEmpty();
        assertThat(creditList).hasSize(1);
        assertThat(creditList).contains(credit.toDTO());
    }

    @Test
    void updateCreditBalance_whenCreditExist() {
        credit.setAmount(BigDecimal.TEN);

        when(creditRepository.findById(1L)).thenReturn(Optional.of(credit));

        creditService.updateCreditBalance(1L, BigDecimal.TEN);

        verify(creditRepository, times(1)).save(credit);
    }

    @Test
    void updateCreditBalance_whenCreditDoesNotExist() {
        when(creditRepository.findById(1L)).thenReturn(Optional.empty());

        creditService.updateCreditBalance(1L, BigDecimal.TEN);

        verify(creditRepository, times(0)).save(credit);
    }
}
