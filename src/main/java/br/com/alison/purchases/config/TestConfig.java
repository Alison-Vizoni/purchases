package br.com.alison.purchases.config;

import br.com.alison.purchases.service.DBService;
import br.com.alison.purchases.service.EmailService;
import br.com.alison.purchases.service.MockEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile("test")
public class TestConfig {

    @Autowired
    private DBService dbService;

    @Bean
    public boolean instantianteDatabase() throws ParseException {
        dbService.instantianceTestDatabase();
        return true;
    }

    @Bean
    public EmailService emailService(){
        return new MockEmailService();
    }
}
