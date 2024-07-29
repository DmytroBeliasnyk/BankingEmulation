package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.repositories.AccountRepository;
import org.emuba.bankingemulation.repositories.ClientRepository;
import org.emuba.bankingemulation.services.impl.AccountNumberGenerator;
import org.emuba.bankingemulation.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {
    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private AccountNumberGenerator numberGenerator;
    private CustomClient client;
    private Account account;

    @BeforeEach
    void init() {
        client = CustomClient.of("Client", "Client",
                "email@email.com", "login", "password", UserRole.USER);
        account = Account.of("123123", TypeCurrency.USD);

        client.addAccount(account);
    }

    @Test
    void addNewAccount_whenAccountExist() {
        when(accountRepository.findByCurrencyAndClient_Login(TypeCurrency.USD, "login"))
                .thenReturn(Optional.empty());
        when(clientRepository.findByLogin("login")).thenReturn(client);
        when(numberGenerator.generateUniqueAccountNumber()).thenReturn("123123");

        accountService.addNewAccount(TypeCurrency.USD, "login");

        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void addNewAccount_whenAccountDoesNotExist() {
        when(accountRepository.findByCurrencyAndClient_Login(TypeCurrency.USD, "login"))
                .thenReturn(Optional.of(account));

        accountService.addNewAccount(TypeCurrency.USD, "login");

        verify(accountRepository, times(0)).save(account);
    }

    @Test
    void findAccount() {
        when(accountRepository.findByCurrencyAndClient_Login(TypeCurrency.USD, "login"))
                .thenReturn(Optional.of(account));

        Optional<Account> optionalAccount = accountService.findAccount(
                TypeCurrency.USD, "login");

        assertThat(optionalAccount).isPresent();
        assertThat(optionalAccount.get()).isEqualTo(account);
    }

    @Test
    void findAllByClient() {
        when(accountRepository.findAllByClient(client)).thenReturn(List.of(account));

        List<AccountDTO> accounts = accountService.findAllByClient(client);

        assertThat(accounts).isNotNull();
        assertThat(accounts).isNotEmpty();
        assertThat(accounts).hasSize(1);
        assertThat(accounts).contains(account.toDTO());
    }

    @Test
    void findAccountByNumber() {
        when(accountRepository.findAccountByAccountNumber("123123"))
                .thenReturn(Optional.of(account));

        Optional<Account> optionalAccount = accountService.findAccountByNumber
                ("123123");

        assertThat(optionalAccount).isPresent();
        assertThat(optionalAccount.get()).isEqualTo(account);
    }

    @Test
    void updateBalance_whenAccountExist() {
        when(accountRepository.findByCurrencyAndClient_Id(TypeCurrency.USD, 1L))
                .thenReturn(Optional.of(account));

        BigDecimal newBalance = BigDecimal.TEN;
        account.setBalance(newBalance);

        accountService.updateBalance(1L, TypeCurrency.USD, newBalance);

        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void updateBalance_whenAccountDoesNotExist() {
        when(accountRepository.findByCurrencyAndClient_Id(TypeCurrency.USD, 1L))
                .thenReturn(Optional.empty());

        accountService.updateBalance(1L, TypeCurrency.USD, BigDecimal.TEN);

        verify(accountRepository, times(0)).save(account);
    }
}
