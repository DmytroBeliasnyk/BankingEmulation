package org.emuba.bankingemulation.controllers;

import org.emuba.bankingemulation.dto.ClientDTO;
import org.emuba.bankingemulation.dto.PageCountDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.Account;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.services.impl.AccountServiceImpl;
import org.emuba.bankingemulation.services.impl.ClientServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ClientServiceImpl clientService;
    private final AccountServiceImpl accountService;
    private final int PAGE_SIZE = 5;

    public AdminController(ClientServiceImpl clientService, AccountServiceImpl accountService) {
        this.clientService = clientService;
        this.accountService = accountService;
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
}
