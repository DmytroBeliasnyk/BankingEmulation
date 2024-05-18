package org.emuba.bankingemulation.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.emuba.bankingemulation.enums.ClientRequestType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Service
public class MailService {
    private final JavaMailSender sender;

    public MailService(JavaMailSender sender) {
        this.sender = sender;
    }

    public void sendEmail(String email, ClientRequestType subject, String text, String attachmentText) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        FileSystemResource attachmentFile = new FileSystemResource(
                createAttachmentFile(subject, attachmentText));

        helper.setTo(email);
        helper.setSubject(subject.name());
        helper.setText(text);
        helper.addAttachment(subject.name(), attachmentFile);

        sender.send(message);
    }

    private File createAttachmentFile(ClientRequestType subject, String attachmentText) {
        File file = new File(subject.name() + ".pdf");

        try (PrintWriter pw = new PrintWriter(file)) {
            pw.print(attachmentText);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

}
