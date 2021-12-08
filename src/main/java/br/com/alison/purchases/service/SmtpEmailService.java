package br.com.alison.purchases.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class SmtpEmailService extends AbstractEmailService{

    @Autowired
    private MailSender mailSender;

    @Autowired
    private JavaMailSender javaMailSender;

    private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);

    @Override
    public void sendEmail(SimpleMailMessage simpleMailMessage) {
        LOG.info("Enviando email...");
        mailSender.send(simpleMailMessage);
        LOG.info("Email enviado.");
    }

    @Override
    public void sendHtmlEmail(MimeMessage mimeMessage) {
        LOG.info("Enviando email...");
        javaMailSender.send(mimeMessage);
        LOG.info("Email enviado.");
    }
}
