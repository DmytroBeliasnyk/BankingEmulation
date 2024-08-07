package org.emuba.bankingemulation.controllers;

import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.dto.PageCountDTO;
import org.emuba.bankingemulation.dto.TransactionDTO;
import org.emuba.bankingemulation.enums.ClientRequestType;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.services.impl.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientServiceImpl clientService;
    private final AccountServiceImpl accountService;
    private final HistoryServiceImpl historyService;
    private final ClientRequestServiceImpl dataRequestService;
    private final SupportingMethodsForControllers supMethod;

    public ClientController(ClientServiceImpl clientService, AccountServiceImpl accountService,
                            HistoryServiceImpl historyService, ClientRequestServiceImpl dataRequestService,
                            SupportingMethodsForControllers supMethod) {
        this.clientService = clientService;
        this.accountService = accountService;
        this.historyService = historyService;
        this.dataRequestService = dataRequestService;
        this.supMethod = supMethod;
    }

    @GetMapping("get_account")
    public ResponseEntity<?> getAccount(@RequestParam String currency) {
        if (supMethod.check(currency) || !currency.equalsIgnoreCase("UAH"))
            return new ResponseEntity<>("Unsupported currency", HttpStatus.BAD_REQUEST);
        TypeCurrency typeOfCurrency = TypeCurrency.valueOf(currency.toUpperCase());

        Optional<Account> accountOpt = accountService.findAccount(
                typeOfCurrency, supMethod.getCurrentUser().getUsername());

        if (accountOpt.isEmpty())
            new ResponseEntity<>("Account was not created", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(accountOpt.map(Account::toDTO).get(), HttpStatus.OK);
    }

    @GetMapping("accounts_list")
    public List<AccountDTO> getAllAccounts() {
        CustomClient client = clientService.findClientByLogin(supMethod.getCurrentUser().getUsername());
        return accountService.findAllByClient(client);
    }

    @PostMapping("do_transaction")
    public ResponseEntity<String> doTransaction(@RequestBody AccountDTO fromAccount,
                                                @RequestParam(name = "to_account") String toAccountNumber,
                                                @RequestParam BigDecimal amount) {


        Optional<Account> optFrom = accountService.findAccountByNumber(fromAccount.getAccountNumber());
        Optional<Account> optTo = accountService.findAccountByNumber(toAccountNumber);

        if (optTo.isEmpty() || optFrom.isEmpty())
            return new ResponseEntity<>("The account number was not found", HttpStatus.BAD_REQUEST);

        Account from = optFrom.get();
        Account to = optTo.get();

        if (from.getBalance().compareTo(amount) < 0 || from.getAccountNumber().equals(to.getAccountNumber()))
            return new ResponseEntity<>("Negative amount", HttpStatus.BAD_REQUEST);

        BigDecimal convertedAmount = amount;
        if (from.getCurrency() != to.getCurrency()) {
            convertedAmount = supMethod.converter(from.getCurrency().toString(),
                    to.getCurrency().toString(), amount);
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(convertedAmount));

        accountService.updateBalance(from.getClient().getId(), from.getCurrency(), from.getBalance());
        accountService.updateBalance(to.getClient().getId(), to.getCurrency(), to.getBalance());

        historyService.saveTransaction(from.getClient(), from.getAccountNumber(), from.getCurrency(),
                to.getClient(), toAccountNumber, to.getCurrency(), LocalDate.now(), amount);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PutMapping("add_account")
    public ResponseEntity<String> addAccount(@RequestParam String currency) {
        if (supMethod.check(currency))
            return new ResponseEntity<>("Unsupported currency", HttpStatus.BAD_REQUEST);

        TypeCurrency typeOfCurrency = TypeCurrency.valueOf(currency.toUpperCase());
        accountService.addNewAccount(typeOfCurrency, supMethod.getCurrentUser().getUsername());

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @GetMapping("transactions")
    public List<TransactionDTO> getTransactions(@RequestParam(required = false, defaultValue = "0")
                                                int page) {
        if (page < 0) page = 0;
        return historyService.findByClientLogin(supMethod.getCurrentUser().getUsername(),
                PageRequest.of(page, 10, Sort.Direction.DESC, "id"));
    }

    @GetMapping("transactions_by_date")
    public List<TransactionDTO> getTransactionsByDate(@RequestParam String date,
                                                      @RequestParam(required = false, defaultValue = "0")
                                                      int page) {
        if (page < 0) page = 0;

        LocalDate dateOfTransaction;
        try {
            dateOfTransaction = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format, expected format is YYYY-MM-DD", e);
        }

        return historyService.findAllByDate(supMethod.getCurrentUser().getUsername(), dateOfTransaction,
                PageRequest.of(page, 10, Sort.Direction.DESC, "id"));
    }

    @GetMapping("transactions_between_date")
    public List<TransactionDTO> getTransactionsBetweenDate(@RequestParam String startDate,
                                                           @RequestParam String endDate,
                                                           @RequestParam(required = false, defaultValue = "0")
                                                           int page) {
        if (page < 0) page = 0;

        LocalDate startDateOfTransaction;
        LocalDate endDateOfTransaction;
        try {
            startDateOfTransaction = LocalDate.parse(startDate);
            endDateOfTransaction = LocalDate.parse(endDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format, expected format is YYYY-MM-DD", e);
        }

        return historyService.findAllBetweenDate(supMethod.getCurrentUser().getUsername(),
                startDateOfTransaction, endDateOfTransaction,
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

        dataRequestService.add(supMethod.getCurrentUser().getUsername(),
                ClientRequestType.TRANSACTION_CONFIRMATION, transactionId);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("account_balance")
    public ResponseEntity<String> getAccountBalance() {
        dataRequestService.add(supMethod.getCurrentUser().getUsername(),
                ClientRequestType.ACCOUNT_BALANCE, null);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
