package org.emuba.bankingemulation.controller;

import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.service.AccountService;
import org.emuba.bankingemulation.service.impl.ClientServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyController {
    private final ClientServiceImpl clientService;
    private final AccountService accountService;
    private final PasswordEncoder encoder;

    public MyController(ClientServiceImpl clientService, AccountService accountService,
                        PasswordEncoder encoder) {
        this.clientService = clientService;
        this.accountService = accountService;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestParam String name,
                                         @RequestParam String surName,
                                         @RequestParam String email,
                                         @RequestParam String login,
                                         @RequestParam String password) {
        if (name == null || surName == null || email == null ||
                login == null || password == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<String> logins = clientService.findAllLogins();
        for (var log : logins) {
            if (log.equals(login))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String passHash = encoder.encode(password);

        clientService.addClient(name, surName, email,
                login, passHash, UserRole.USER);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/get_account")
    public ResponseEntity<AccountDTO> getAccount(@RequestParam String currency) {
        if (check(currency))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        TypeCurrency typeOfCurrency = TypeCurrency.valueOf(currency.toUpperCase());
        User user = getCurrentUser();
        return new ResponseEntity<>(accountService.findAccount(typeOfCurrency, user.getUsername()),
                HttpStatus.OK);
    }

    @GetMapping("/add_account")
    public ResponseEntity<Void> addAccount(@RequestParam String currency) {
        if (check(currency))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        TypeCurrency typeOfCurrency = TypeCurrency.valueOf(currency.toUpperCase());
        User user = getCurrentUser();
        accountService.addNewAccount(typeOfCurrency, getCurrentUser().getUsername());
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
}
