package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.Order;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void sendOrderConfirmationEmail(Order order);

    void sendEmail(SimpleMailMessage simpleMailMessage);
}
