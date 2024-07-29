package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.enums.ClientRequestType;
import org.emuba.bankingemulation.services.impl.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MailServiceTests {
    @InjectMocks
    private MailService mailService;
    @Mock
    private JavaMailSender sender;

    @Test
    void sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("email@email.com");
        message.setSubject(ClientRequestType.CREDIT_INFO.name());
        message.setText("text");

        mailService.sendEmail("email@email.com", ClientRequestType.CREDIT_INFO, "text");

        verify(sender, times(1)).send(message);
    }
}
