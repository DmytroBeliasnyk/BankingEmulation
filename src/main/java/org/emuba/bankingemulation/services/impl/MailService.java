package org.emuba.bankingemulation.services.impl;

import org.emuba.bankingemulation.enums.ClientRequestType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender sender;

    public MailService(JavaMailSender sender) {
        this.sender = sender;
    }

    public void sendEmail(String email, ClientRequestType subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject(subject.name());
        message.setText(text);

        sender.send(message);
    }
}
