package br.com.alison.purchases.resources;

import br.com.alison.purchases.domain.Client;
import br.com.alison.purchases.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/clients")
public class ClientResources {

    @Autowired
    private ClientService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Client client = service.findClientById(id);
        return ResponseEntity.ok().body(client);
    }
}
