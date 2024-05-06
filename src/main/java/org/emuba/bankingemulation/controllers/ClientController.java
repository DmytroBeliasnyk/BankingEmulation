package org.emuba.bankingemulation.controllers;

import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.dto.CurrencyRateDTO;
import org.emuba.bankingemulation.dto.PageCountDTO;
import org.emuba.bankingemulation.dto.TransactionDTO;
import org.emuba.bankingemulation.enums.ClientRequestType;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.retrievers.CurrencyRatesRetriever;
import org.emuba.bankingemulation.services.impl.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientServiceImpl clientService;
    private final AccountServiceImpl accountService;
    private final RateServiceImpl rateService;
    private final HistoryServiceImpl historyService;
    private final ClientRequestServiceImpl dataRequestService;
    private final CurrencyRatesRetriever retriever;
    private final PasswordEncoder encoder;

    public ClientController(ClientServiceImpl clientService, AccountServiceImpl accountService,
                            RateServiceImpl rateService, HistoryServiceImpl historyService, ClientRequestServiceImpl dataRequestService,
                            CurrencyRatesRetriever retriever, PasswordEncoder encoder) {
        this.clientService = clientService;
        this.accountService = accountService;
        this.rateService = rateService;
        this.historyService = historyService;
        this.dataRequestService = dataRequestService;
        this.retriever = retriever;
        this.encoder = encoder;
    }

    @GetMapping("get_account")
    public ResponseEntity<?> getAccount(@RequestParam String currency) {
        if (check(currency))
            return new ResponseEntity<>("Unsupported currency", HttpStatus.BAD_REQUEST);
        TypeCurrency typeOfCurrency = TypeCurrency.valueOf(currency.toUpperCase());

        Optional<Account> accountOpt = accountService.findAccount(
                typeOfCurrency, getCurrentUser().getUsername());

        if (accountOpt.isEmpty())
            new ResponseEntity<>("Account was not created", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(accountOpt.map(Account::toDTO).get(), HttpStatus.OK);
    }

    @GetMapping("accounts_list")
    public List<AccountDTO> getAllAccounts() {
        CustomClient client = clientService.findClientByLogin(getCurrentUser().getUsername());
        return accountService.findAllByClient(client);
    }

    @PostMapping("do_transaction")
    public ResponseEntity<String> doTransaction(@RequestBody AccountDTO fromAccount,
                                                @RequestParam(name = "to_account") String toAccountNumber,
                                                @RequestParam double amount) {


        Optional<Account> optFrom = accountService.findAccountByNumber(fromAccount.getAccountNumber());
        Optional<Account> optTo = accountService.findAccountByNumber(toAccountNumber);

        if (optTo.isEmpty() || optFrom.isEmpty())
            return new ResponseEntity<>("The account number was not found", HttpStatus.BAD_REQUEST);

        Account from = optFrom.get();
        Account to = optTo.get();

        if (from.getBalance() < amount || from.getAccountNumber().equals(to.getAccountNumber()))
            return new ResponseEntity<>("Negative amount", HttpStatus.BAD_REQUEST);

        double convertedAmount = amount;
        if (from.getCurrency() != to.getCurrency()) {
            convertedAmount = converter(from.getCurrency().toString(), to.getCurrency().toString(),
                    LocalDate.now(), amount);
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + convertedAmount);

        accountService.updateBalance(from.getClient().getId(), from.getCurrency(), from.getBalance());
        accountService.updateBalance(to.getClient().getId(), to.getCurrency(), to.getBalance());

        historyService.saveTransaction(from.getClient(), from.getAccountNumber(), from.getCurrency(),
                to.getClient(), toAccountNumber, to.getCurrency(), LocalDateTime.now(), amount);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PutMapping("add_account")
    public ResponseEntity<String> addAccount(@RequestParam String currency) {
        if (check(currency))
            return new ResponseEntity<>("Unsupported currency", HttpStatus.BAD_REQUEST);

        TypeCurrency typeOfCurrency = TypeCurrency.valueOf(currency.toUpperCase());
        accountService.addNewAccount(typeOfCurrency, getCurrentUser().getUsername());

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @GetMapping("transactions")
    public List<TransactionDTO> getTransactions(@RequestParam(required = false, defaultValue = "0")
                                                int page) {
        return historyService.findByClientLogin(getCurrentUser().getUsername(),
                PageRequest.of(page, 10, Sort.Direction.DESC, "id"));
    }

    @GetMapping("pages_transactions")
    public PageCountDTO pages() {
        long transactions = historyService.count();
        long pageCount = (transactions / 10) + ((transactions % 10 == 0) ? 0 : 1);
        return PageCountDTO.of(pageCount, 10);
    }

    @GetMapping("transaction_confirmation")
    public ResponseEntity<String> getTransactionConfirmation(@RequestParam Long transactionId) {
        if (transactionId == null)
            return new ResponseEntity<>("Not Selected", HttpStatus.BAD_REQUEST);

        dataRequestService.add(getCurrentUser().getUsername(),
                ClientRequestType.TRANSACTION_CONFIRMATION, transactionId);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("account_balance")
    public ResponseEntity<String> getAccountBalance() {
        dataRequestService.add(getCurrentUser().getUsername(),
                ClientRequestType.ACCOUNT_BALANCE, null);
        return new ResponseEntity<>("Success", HttpStatus.OK);
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
