package org.emuba.bankingemulation.controllers;

import org.emuba.bankingemulation.dto.ClientDTO;
import org.emuba.bankingemulation.dto.ClientRequestDTO;
import org.emuba.bankingemulation.dto.PageCountDTO;
import org.emuba.bankingemulation.enums.ClientRequestType;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.ClientRequest;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.services.impl.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ClientServiceImpl clientService;
    private final AccountServiceImpl accountService;
    private final ClientRequestServiceImpl clientRequestService;
    private final HistoryServiceImpl historyService;
    private final MailService mailService;
    private final int PAGE_SIZE = 5;

    public AdminController(ClientServiceImpl clientService, AccountServiceImpl accountService,
                           ClientRequestServiceImpl clientRequestService, HistoryServiceImpl historyService,
                           MailService mailService) {
        this.clientService = clientService;
        this.accountService = accountService;
        this.clientRequestService = clientRequestService;
        this.historyService = historyService;
        this.mailService = mailService;
    }

    @GetMapping("clients")
    public List<ClientDTO> getUsers(@RequestParam(required = false, defaultValue = "0")
                                    int page) {
        if (page < 0) page = 0;
        return clientService.findAllClients(PageRequest.of(page, PAGE_SIZE,
                Sort.Direction.ASC, "id"));
    }

    @GetMapping("pages")
    public PageCountDTO pages() {
        long clients = clientService.countClients();
        long pageCount = (clients / PAGE_SIZE) + ((clients % PAGE_SIZE == 0) ? 0 : 1);
        return PageCountDTO.of(pageCount, PAGE_SIZE);
    }

    @PutMapping("add_money")
    public ResponseEntity<String> addMoney(@RequestParam Long clientId,
                                           @RequestParam BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            return new ResponseEntity<>("Negative value", HttpStatus.BAD_REQUEST);

        CustomClient client = clientService.findById(clientId);
        Account account = accountService.
                findAccount(TypeCurrency.UAH, client.getLogin()).get();
        account.setBalance(account.getBalance().add(amount));

        accountService.updateBalance(clientId, TypeCurrency.UAH, account.getBalance());
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("confirmations")
    public List<ClientRequestDTO> getConfirmations(@RequestParam(required = false, defaultValue = "0")
                                                   int page) {
        if (page < 0) page = 0;
        return clientRequestService.getAll(PageRequest.of(page, PAGE_SIZE, Sort.Direction.ASC, "id"));
    }

    @GetMapping("confirmations_by_type")
    public List<ClientRequestDTO> getConfirmationsByType(@RequestParam(required = false, defaultValue = "0")
                                                         int page,
                                                         @RequestParam String requestType) {
        if (page < 0) page = 0;

        ClientRequestType clientRequestType;
        try {
            clientRequestType = ClientRequestType.valueOf(requestType);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid request type");
        }

        return clientRequestService.getAllByType(clientRequestType,
                PageRequest.of(page, PAGE_SIZE, Sort.Direction.ASC, "id"));
    }

    @GetMapping("pages_confirmations")
    public PageCountDTO pagesConfirmations() {
        long count = clientRequestService.count();
        long pageCount = (count / PAGE_SIZE) + ((count % PAGE_SIZE == 0) ? 0 : 1);
        return PageCountDTO.of(pageCount, PAGE_SIZE);
    }

    @GetMapping("send")
    public ResponseEntity<String> sendData(@RequestParam Long dataId) {
        if (dataId == null)
            return new ResponseEntity<>("Not Selected", HttpStatus.BAD_REQUEST);

        ClientRequest request = clientRequestService.find(dataId);
        CustomClient client = request.getClient();

        if (request.getClientRequestType() == ClientRequestType.ACCOUNT_BALANCE) {
            StringBuilder sb = new StringBuilder();
            for (var account : accountService.findAllByClient(client)) {
                sb.append(account.toString())
                        .append(System.lineSeparator());
            }
            mailService.sendEmail(
                    client.getEmail(),
                    ClientRequestType.ACCOUNT_BALANCE,
                    sb.toString());
        } else {
            mailService.sendEmail(
                    client.getEmail(),
                    ClientRequestType.TRANSACTION_CONFIRMATION,
                    historyService.find(dataId).toString());
        }
        clientRequestService.delete(request.getId());

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
