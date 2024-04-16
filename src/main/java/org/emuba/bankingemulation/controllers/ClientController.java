package org.emuba.bankingemulation.controllers;

import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.dto.CurrencyRateDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.retrievers.CurrencyRatesRetriever;
import org.emuba.bankingemulation.services.AccountService;
import org.emuba.bankingemulation.services.impl.AccountServiceImpl;
import org.emuba.bankingemulation.services.impl.ClientServiceImpl;
import org.emuba.bankingemulation.services.impl.RateServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class ClientController {
    private final ClientServiceImpl clientService;
    private final AccountServiceImpl accountService;
    private final RateServiceImpl rateService;
    private final CurrencyRatesRetriever retriever;
    private final PasswordEncoder encoder;

    public ClientController(ClientServiceImpl clientService, AccountServiceImpl accountService, RateServiceImpl rateService, CurrencyRatesRetriever retriever,
                            PasswordEncoder encoder) {
        this.clientService = clientService;
        this.accountService = accountService;
        this.rateService = rateService;
        this.retriever = retriever;
        this.encoder = encoder;
    }

    @GetMapping("/register")
    public ResponseEntity<Void> register(@RequestParam String name,
                                         @RequestParam String surname,
                                         @RequestParam String email,
                                         @RequestParam String login,
                                         @RequestParam String password) {
        if (name == null || surname == null || email == null ||
                login == null || password == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<String> logins = clientService.findAllLogins();
        for (var log : logins) {
            if (log.equals(login))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String passHash = encoder.encode(password);

        clientService.addClient(name, surname, email,
                login, passHash, UserRole.USER);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/get_account")
    public ResponseEntity<AccountDTO> getAccount(@RequestParam String currency) {
        if (check(currency))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        TypeCurrency typeOfCurrency = TypeCurrency.valueOf(currency.toUpperCase());

        Optional<Account> accountOpt = accountService.findAccount(
                typeOfCurrency, getCurrentUser().getUsername());

        return accountOpt.map(account -> new ResponseEntity<>(account.toDTO(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/add_account")
    public ResponseEntity<Void> addAccount(@RequestParam String currency) {
        if (check(currency))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        TypeCurrency typeOfCurrency = TypeCurrency.valueOf(currency.toUpperCase());
        accountService.addNewAccount(typeOfCurrency, getCurrentUser().getUsername());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("do_transaction")
    public ResponseEntity<Void> doTransaction(@RequestParam(name = "currency") String fromCurrency,
                                              @RequestParam(name = "account_number") String toAccountNumber,
                                              @RequestParam(name = "amount") double amount) {
        if (fromCurrency == null || toAccountNumber == null
                || amount < 5 || check(fromCurrency))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Account> optFrom = accountService.findAccount(
                TypeCurrency.valueOf(fromCurrency.toUpperCase()), getCurrentUser().getUsername());
        Optional<Account> optTo = accountService.findAccountByNumber(toAccountNumber);

        if (optFrom.isEmpty() || optTo.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Account from = optFrom.get();
        Account to = optTo.get();

        if (from.getBalance() < amount || from.getAccountNumber().equals(to.getAccountNumber()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        double convertedAmount = amount;
        if (from.getCurrency() != to.getCurrency()) {
            convertedAmount = converter(from.getCurrency().toString(), to.getCurrency().toString(),
                    LocalDate.now(), amount);
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + convertedAmount);

        accountService.updateBalance(from.getClient().getId(), TypeCurrency.valueOf(fromCurrency.toUpperCase()),
                from.getBalance());
        accountService.updateBalance(to.getClient().getId(), to.getCurrency(), to.getBalance());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private boolean check(String currency) {
        if (currency == null)
            return true;
        try {
            TypeCurrency typeOfCurrency = TypeCurrency.valueOf(currency.toUpperCase());
        } catch (IllegalArgumentException e) {
            return true;
        }
        return false;
    }

    private double converter(String fromCurrency, String toCurrency, LocalDate date,
                             double amount) {
        double rateToUAH = 1;
        double rateFromUAH = 1;

        if (!fromCurrency.equals("UAH")) {
            CurrencyRateDTO rate = rateService.find(fromCurrency, LocalDate.now());
            if (rate == null)
                rate = retriever.getRate(fromCurrency, LocalDate.now());
            rateToUAH = rate.getRate();
        }
        if (!toCurrency.equals("UAH")) {
            CurrencyRateDTO rate = rateService.find(toCurrency, LocalDate.now());
            if (rate == null)
                rate = retriever.getRate(toCurrency, LocalDate.now());
            rateFromUAH = rate.getRate();
        }

        return amount * rateToUAH / rateFromUAH;
    }
}