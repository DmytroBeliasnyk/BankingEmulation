package org.emuba.bankingemulation.configs;

import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.services.impl.ClientServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
    @Bean
    public CommandLineRunner commandLineRunner(final ClientServiceImpl clientService,
                                               final PasswordEncoder encoder) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                clientService.addClient("Admin", "Admin", null,
                        "admin", encoder.encode("admin"), UserRole.ADMIN);
                clientService.addClient("User", "User", null,
                        "user", encoder.encode("user"), UserRole.USER);
            }
        };
    }
}
