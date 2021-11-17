package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.Client;
import br.com.alison.purchases.repository.ClientRepository;
import br.com.alison.purchases.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repositry;

    public Client findClientById(Long id){
        Optional<Client> category = repositry.findById(id);

        return category.orElseThrow(() -> new ObjectNotFoundException(new StringBuilder()
                .append("Object not found! Id: ")
                .append(id)
                .append(", type: ")
                .append(Client.class.getName()).toString()));
    }
}
