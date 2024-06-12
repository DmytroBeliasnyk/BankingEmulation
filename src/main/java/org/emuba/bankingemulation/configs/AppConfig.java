package org.emuba.bankingemulation.configs;

import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.services.impl.ClientServiceImpl;
import org.emuba.bankingemulation.services.impl.HistoryServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Properties;

@PropertySource("classpath:email_config.txt")
@Configuration
public class AppConfig {
    @Value("${EMAIL_USERNAME}")
    private String EMAIL_USERNAME;
    @Value("${EMAIL_PASSWORD}")
    private String EMAIL_PASSWORD;

    @Bean
    public CommandLineRunner commandLineRunner(final ClientServiceImpl clientService,
                                               final PasswordEncoder encoder,
                                               final HistoryServiceImpl historyService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                clientService.addClient("Admin", "Admin", null,
                        "admin", encoder.encode("123admin"), UserRole.ADMIN);
                clientService.addClient("Dmytro", "Beliasnyk", "dimabelasnik6@gmail.com",
                        "user", encoder.encode("123user"), UserRole.USER);

                CustomClient client1 = clientService.findClientByLogin("user");
                historyService.saveTransaction(client1, "123123", TypeCurrency.USD,
                        client1, "123123", TypeCurrency.USD, LocalDate.of(2024, 5, 10), BigDecimal.TEN);
                historyService.saveTransaction(client1, "123123", TypeCurrency.USD,
                        client1, "123123", TypeCurrency.USD, LocalDate.of(2024, 5, 15), BigDecimal.TEN);
                historyService.saveTransaction(client1, "123123", TypeCurrency.USD,
                        client1, "123123", TypeCurrency.USD, LocalDate.now(), BigDecimal.TEN);
            }
        };
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(EMAIL_USERNAME);
        mailSender.setPassword(EMAIL_PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

//    @Bean
//    public BasicDataSource dataSource() throws URISyntaxException {
//        String dbUrl = System.getenv("JDBC_DATABASE_URL");
//        String username = System.getenv("JDBC_DATABASE_USERNAME");
//        String password = System.getenv("JDBC_DATABASE_PASSWORD");
//
//        BasicDataSource basicDataSource = new BasicDataSource();
//        basicDataSource.setUrl(dbUrl);
//        basicDataSource.setUsername(username);
//        basicDataSource.setPassword(password);
//
//        return basicDataSource;
//    }
}

