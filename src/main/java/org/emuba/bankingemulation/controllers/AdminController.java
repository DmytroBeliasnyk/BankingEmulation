package org.emuba.bankingemulation.controllers;

import org.emuba.bankingemulation.dto.ClientDTO;
import org.emuba.bankingemulation.dto.DataRequestDTO;
import org.emuba.bankingemulation.dto.PageCountDTO;
import org.emuba.bankingemulation.enums.DataType;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.models.DataRequest;
import org.emuba.bankingemulation.services.impl.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ClientServiceImpl clientService;
    private final AccountServiceImpl accountService;
    private final DataRequestServiceImpl dataRequestService;
    private final HistoryServiceImpl historyService;
    private final MailService mailService;
    private final int PAGE_SIZE = 5;

    public AdminController(ClientServiceImpl clientService, AccountServiceImpl accountService, DataRequestServiceImpl dataRequestService, HistoryServiceImpl historyService, MailService mailService) {
        this.clientService = clientService;
        this.accountService = accountService;
        this.dataRequestService = dataRequestService;
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

    @GetMapping("add_money")
    public ResponseEntity<Void> addMoney(@RequestParam Long clientId,
                                         @RequestParam double amount) {
        if (amount <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        CustomClient client = clientService.findById(clientId);
        Account account = accountService.
                findAccount(TypeCurrency.UAH, client.getLogin()).get();
        account.setBalance(account.getBalance() + amount);

        accountService.updateBalance(clientId, TypeCurrency.UAH, account.getBalance());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("confirmations")
    public List<DataRequestDTO> getConfirmations() {
        return dataRequestService.getAll();
    }

    @GetMapping("send")
    public ResponseEntity<Void> sendData(@RequestParam Long dataId) {
        if (dataId == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        DataRequest request = dataRequestService.find(dataId);
        CustomClient client = request.getClient();

        if (request.getDataType() == DataType.ACCOUNT_BALANCE) {
            StringBuilder sb = new StringBuilder();
            for (var a : accountService.findAllByClient(client)) {
                sb.append(a.toString())
                        .append(System.lineSeparator());
            }
            mailService.sendEmail(client.getEmail(),
                    DataType.ACCOUNT_BALANCE, sb.toString());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            mailService.sendEmail(client.getEmail(),
                    DataType.TRANSACTION_CONFIRMATION,
                    historyService.find(dataId).toString());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
