package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.CustomClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AccountRepositoryTests {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    private CustomClient client;
    private Account account = Account.of("123123", TypeCurrency.UAH);

    @BeforeEach
    void init() {
        client = clientRepository.save(CustomClient.of("Client", "Client",
                "email@email.com", "login", "password", UserRole.USER));
        client.addAccount(account);

        account = accountRepository.save(account);
    }

    @Test
    void findByCurrencyAndClient_Login() {
        Optional<Account> optionalAccount = accountRepository.findByCurrencyAndClient_Login(
                TypeCurrency.UAH, "login");

        assertThat(optionalAccount).isPresent();
        assertThat(optionalAccount.get().getId()).isGreaterThan(0L);
        assertThat(optionalAccount.get().getClient()).isEqualTo(client);
    }

    @Test
    void findByCurrencyAndClient_Id() {
        Optional<Account> optionalAccount = accountRepository.findByCurrencyAndClient_Id(
                TypeCurrency.UAH, client.getId());

        assertThat(optionalAccount).isPresent();
        assertThat(optionalAccount.get()).isEqualTo(account);
    }

    @Test
    void findAccountByAccountNumber() {
        Optional<Account> optionalAccount = accountRepository.findAccountByAccountNumber("123123");

        assertThat(optionalAccount).isPresent();
        assertThat(optionalAccount.get()).isEqualTo(account);
    }

    @Test
    void findAllByClient() {
        Account usdAccount = Account.of("456456", TypeCurrency.USD);
        client.addAccount(usdAccount);
        usdAccount = accountRepository.save(usdAccount);

        List<Account> accounts = accountRepository.findAllByClient(client);

        assertThat(accounts).isNotNull();
        assertThat(accounts).isNotEmpty();
        assertThat(accounts).hasSize(2);
        assertThat(accounts).contains(account, usdAccount);
    }

    @Test
    void existsByAccountNumber() {
        Boolean existsAccount = accountRepository.existsByAccountNumber("123123");

        assertThat(existsAccount).isNotNull();
        assertThat(existsAccount).isTrue();
    }
}
