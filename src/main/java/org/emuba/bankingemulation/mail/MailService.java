package org.emuba.bankingemulation.mail;

import org.emuba.bankingemulation.enums.DataType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class MailService {
    private final JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public MailService(JavaMailSender sender) {
        this.sender = sender;
    }

    public void sendEmail(String email, DataType subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setFrom(emailFrom);
        message.setSubject(subject.name());
        message.setText(text);

        sender.send(message);
    }
}
