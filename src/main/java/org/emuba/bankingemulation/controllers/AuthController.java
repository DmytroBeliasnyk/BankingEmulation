package org.emuba.bankingemulation.controllers;

import org.emuba.bankingemulation.configs.EmailUtils;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.services.impl.ClientServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final ClientServiceImpl clientService;
    private final PasswordEncoder encoder;

    public AuthController(ClientServiceImpl clientService, PasswordEncoder encoder) {
        this.clientService = clientService;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String name,
                                           @RequestParam String surname,
                                           @RequestParam String email,
                                           @RequestParam String login,
                                           @RequestParam String password) {
        if (clientService.existsByLogin(login))
            return new ResponseEntity<>("Login already used", HttpStatus.BAD_REQUEST);

        if (!EmailUtils.isValidEmailAddress(email))
            return new ResponseEntity<>("Invalid email address", HttpStatus.BAD_REQUEST);

        String passHash = encoder.encode(password);

        clientService.addClient(name, surname, email,
                login, passHash, UserRole.USER);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }
}
