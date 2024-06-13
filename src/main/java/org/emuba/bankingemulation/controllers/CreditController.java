package org.emuba.bankingemulation.controllers;

import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.dto.CreditDTO;
import org.emuba.bankingemulation.enums.ClientRequestType;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.Credit;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.services.impl.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/client/credits")
public class CreditController {
    private final CreditServiceImpl creditService;
    private final AccountServiceImpl accountService;
    private final ClientServiceImpl clientService;
    private final SupportingMethodsForControllers supMethod;
    private final MailService mailService;

    public CreditController(CreditServiceImpl creditService, AccountServiceImpl accountService,
                            ClientServiceImpl clientService, SupportingMethodsForControllers supMethod, MailService mailService) {
        this.creditService = creditService;
        this.accountService = accountService;
        this.clientService = clientService;
        this.supMethod = supMethod;
        this.mailService = mailService;
    }

    @PostMapping("take_credit")
    public ResponseEntity<String> createCredit(@RequestParam String endOfCredit,
                                               @RequestParam BigDecimal amount,
                                               @RequestParam String currency) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            return new ResponseEntity<>("Negative amount", HttpStatus.BAD_REQUEST);

        if (supMethod.check(currency))
            return new ResponseEntity<>("Unsupported currency", HttpStatus.BAD_REQUEST);
        TypeCurrency creditCurrency = TypeCurrency.valueOf(currency.toUpperCase());

        LocalDate endDate;
        try {
            endDate = LocalDate.parse(endOfCredit);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format, expected format is YYYY-MM-DD", e);
        }

        if (endDate.until(LocalDate.now()).getMonths() >= 1)
            return new ResponseEntity<>("Invalid date", HttpStatus.BAD_REQUEST);

        CustomClient client = clientService.findClientByLogin(supMethod.getCurrentUser().getUsername());

        BigDecimal convertedAmount = amount;
        if (!creditCurrency.equals(TypeCurrency.UAH)) {
            convertedAmount = supMethod.converter(creditCurrency.name(), TypeCurrency.UAH.name(), amount);
        }

        CreditDTO newCredit = creditService.save(client.getLogin(), LocalDate.now(), endDate,
                amount, creditCurrency);

        Account account = accountService.findAccount(TypeCurrency.UAH, client.getLogin()).get();
        accountService.updateBalance(client.getId(), TypeCurrency.UAH,
                account.getBalance().add(convertedAmount));

        mailService.sendEmail(account.getClient().getEmail(), ClientRequestType.CREDIT_INFO,
                newCredit.toString());
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @GetMapping("show_credits")
    public List<CreditDTO> getAllCredits() {
        return creditService.findAllCredits(supMethod.getCurrentUser().getUsername());
    }

    @PostMapping("pay_credit")
    public ResponseEntity<String> toPayCredit(@RequestBody CreditDTO credit,
                                              @RequestBody AccountDTO fromAccount,
                                              @RequestParam BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            return new ResponseEntity<>("Negative amount", HttpStatus.BAD_REQUEST);

        BigDecimal convertedAmount = amount;
        if (!credit.getCurrency().equals(fromAccount.getCurrency()))
            convertedAmount = supMethod.converter(fromAccount.getCurrency().name(),
                    credit.getCurrency().name(), amount);

        if (convertedAmount.compareTo(fromAccount.getBalance()) < 0)
            return new ResponseEntity<>("Negative amount", HttpStatus.BAD_REQUEST);

        accountService.updateBalance(clientService.findClientByLogin(supMethod.getCurrentUser().getUsername()).getId(),
                fromAccount.getCurrency(), fromAccount.getBalance().subtract(amount));

        BigDecimal newCreditBalance = credit.getAmount().subtract(convertedAmount);
        if (newCreditBalance.compareTo(BigDecimal.ZERO) == 0)
            creditService.deleteCredit(credit.getId());

        creditService.updateCreditBalance(credit.getId(), newCreditBalance);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
