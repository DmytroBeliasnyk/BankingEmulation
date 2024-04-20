package org.emuba.bankingemulation.configs;

import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.services.impl.ClientServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

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
                clientService.addClient("Dmytro", "Beliasnyk", "dimabelasnik6@gmail.com",
                        "user", encoder.encode("user"), UserRole.USER);
            }
        };
    }
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("emubank.bot@gmail.com");
        mailSender.setPassword("kdwmyrikrdqjtjop");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
