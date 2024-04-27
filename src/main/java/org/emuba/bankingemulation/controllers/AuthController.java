package org.emuba.bankingemulation.controllers;

import org.emuba.bankingemulation.configs.EmailUtils;
import org.emuba.bankingemulation.configs.security.JWTGenerator;
import org.emuba.bankingemulation.dto.auth.LoginDTO;
import org.emuba.bankingemulation.dto.auth.RegisterDTO;
import org.emuba.bankingemulation.dto.auth.TokenDTO;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.services.impl.ClientServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final ClientServiceImpl clientService;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;
    private final PasswordEncoder encoder;

    public AuthController(ClientServiceImpl clientService, AuthenticationManager authenticationManager, JWTGenerator jwtGenerator, PasswordEncoder encoder) {
        this.clientService = clientService;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        if (clientService.existsByLogin(registerDTO.getLogin()))
            return new ResponseEntity<>("Login already used", HttpStatus.BAD_REQUEST);

        if (!EmailUtils.isValidEmailAddress(registerDTO.getEmail()))
            return new ResponseEntity<>("Invalid email address", HttpStatus.BAD_REQUEST);

        String passHash = encoder.encode(registerDTO.getPassword());

        clientService.addClient(
                registerDTO.getName(),
                registerDTO.getSurname(),
                registerDTO.getEmail(),
                registerDTO.getLogin(),
                passHash, UserRole.USER);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication);

        return new ResponseEntity<>(new TokenDTO(token), HttpStatus.OK);
    }
}
