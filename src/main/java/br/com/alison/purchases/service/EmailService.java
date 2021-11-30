package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.Client;
import br.com.alison.purchases.domain.Order;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.internet.MimeMessage;

public interface EmailService {

    void sendOrderConfirmationEmail(Order order);

    void sendEmail(SimpleMailMessage simpleMailMessage);

    void sendOrderConfirmationHtmlEmail(Order order);

    void sendHtmlEmail(MimeMessage mimeMessage);

    void sendNewPasswordEmail(Client client, String newPassword);
}
