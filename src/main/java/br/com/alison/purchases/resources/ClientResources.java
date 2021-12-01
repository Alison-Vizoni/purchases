package br.com.alison.purchases.resources;

import br.com.alison.purchases.domain.Client;
import br.com.alison.purchases.dto.ClientDTO;
import br.com.alison.purchases.dto.ClientNewDTO;
import br.com.alison.purchases.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/clients")
public class ClientResources {

    @Autowired
    private ClientService service;

    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable Long id){
        Client client = service.findClientById(id);
        return ResponseEntity.ok().body(client);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<ClientDTO>> findAll(){
        List<Client> categories = service.findAll();
        List<ClientDTO> categoriesDTO = categories.stream()
                .map(ClientDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(categoriesDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/page")
    public ResponseEntity<Page<ClientDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction){
        Page<Client> categories = service.findPage(page, linesPerPage, orderBy, direction);
        Page<ClientDTO> categoriesDTO = categories.map(ClientDTO::new);
        return ResponseEntity.ok().body(categoriesDTO);
    }

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody ClientNewDTO clientNewDTO){
        Client client = service.fromDTO(clientNewDTO);
        client = service.insert(client);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(client.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody  ClientDTO clientDTO, @PathVariable Long id){
        Client client = service.fromDTO(clientDTO);
        client.setId(id);
        service.update(client);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
