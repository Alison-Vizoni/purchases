package br.com.alison.purchases.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.internet.MimeMessage;

public class SmtpEmailService extends AbstractEmailService{

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    private final Dotenv dotenv = Dotenv.configure().load();

    private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

    @Override
    public void sendEmail(SimpleMailMessage simpleMailMessage) {
        LOG.info("Enviando email...");
        javaMailSender.setPassword(dotenv.get("PASSWORD"));
        javaMailSender.send(simpleMailMessage);
        LOG.info("Email enviado.");
    }

    @Override
    public void sendHtmlEmail(MimeMessage mimeMessage) {
        LOG.info("Enviando email...");
        javaMailSender.setPassword(dotenv.get("PASSWORD"));
        javaMailSender.send(mimeMessage);
        LOG.info("Email enviado.");
    }
}
