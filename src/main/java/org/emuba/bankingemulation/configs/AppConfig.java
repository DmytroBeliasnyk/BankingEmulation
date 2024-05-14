package org.emuba.bankingemulation.configs;

import org.apache.commons.dbcp2.BasicDataSource;
import org.emuba.bankingemulation.enums.UserRole;
import org.emuba.bankingemulation.services.impl.ClientServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URISyntaxException;
import java.util.Properties;

@Configuration
@EnableWebMvc
public class AppConfig implements WebMvcConfigurer {
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

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
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

    @Bean
    public BasicDataSource dataSource() throws URISyntaxException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        String username = System.getenv("JDBC_DATABASE_USERNAME");
        String password = System.getenv("JDBC_DATABASE_PASSWORD");

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
    }
}
