package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.Client;
import br.com.alison.purchases.repository.ClientRepository;
import br.com.alison.purchases.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    private Random random = new Random();

    public void sendNewPassword(String email){
        Client client = clientRepository.findByEmail(email);
        if(client == null){
            throw new ObjectNotFoundException("Email not found");
        }

        String newPassword = newPassword();
        client.setPassword(bCryptPasswordEncoder.encode(newPassword));

        clientRepository.save(client);
        emailService.sendNewPasswordEmail(client, newPassword);

    }

    private String newPassword() {
        char[] vetor = new char[10];
        for (int i=0; i<10; i++){
            vetor[i] = randomChar();
        }
        return new String(vetor);
    }

    private char randomChar() {
        int option = random.nextInt(3);
        if(option == 0){
            return (char) (random.nextInt(10) + 48);
        } else if(option == 1){
            return (char) (random.nextInt(26) + 65);
        } else {
            return (char) (random.nextInt(26) + 97);
        }
    }
}
