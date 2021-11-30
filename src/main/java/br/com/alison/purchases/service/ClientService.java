package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.Address;
import br.com.alison.purchases.domain.City;
import br.com.alison.purchases.domain.Client;
import br.com.alison.purchases.domain.enums.ClientType;
import br.com.alison.purchases.domain.enums.Profile;
import br.com.alison.purchases.dto.ClientDTO;
import br.com.alison.purchases.dto.ClientNewDTO;
import br.com.alison.purchases.repository.AddressRepository;
import br.com.alison.purchases.repository.ClientRepository;
import br.com.alison.purchases.security.UserSpringSecurity;
import br.com.alison.purchases.service.exceptions.AuthorizationException;
import br.com.alison.purchases.service.exceptions.DataIntegrityException;
import br.com.alison.purchases.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repositry;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepository addressRepository;

    public Client findClientById(Long id){

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if(userSpringSecurity == null ||
                !userSpringSecurity.hasRole(Profile.ADMIN) && !id.equals(userSpringSecurity.getId())){
            throw new AuthorizationException("Access denied");
        }

        Optional<Client> client = repositry.findById(id);

        return client.orElseThrow(() -> new ObjectNotFoundException(new StringBuilder()
                .append("Object not found! Id: ")
                .append(id)
                .append(", type: ")
                .append(Client.class.getName()).toString()));
    }

    @Transactional
    public Client insert(Client client){
        client.setId(null);
        client = repositry.save(client);
        addressRepository.saveAll(client.getAdresses());
        return client;
    }

    public Client update(Client client) {
        Client newClient = findClientById(client.getId());
        updateData(newClient, client);
        return repositry.save(newClient);
    }

    public void delete(Long id) {
        findClientById(id);
        try {
            repositry.deleteById(id);
        } catch (DataIntegrityViolationException e){
            throw  new DataIntegrityException("Cannot delete a client that has relation entities.");
        }
    }

    public List<Client> findAll() {
        return repositry.findAll();
    }

    public Page<Client> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repositry.findAll(pageRequest);
    }

    public Client fromDTO(ClientDTO clientDTO){
        return new Client(clientDTO.getId(), clientDTO.getName(), clientDTO.getEmail(), null, null, null);
    }

    public Client fromDTO(ClientNewDTO clientNewDTO){
        Client client = new Client(null, clientNewDTO.getName(), clientNewDTO.getEmail(),
                clientNewDTO.getCpfOrCnpj(), ClientType.toEnum(clientNewDTO.getClientType()),
                passwordEncoder.encode(clientNewDTO.getPassword()));

        City city = new City(clientNewDTO.getIdCity(), null, null);

        Address address = new Address(null, clientNewDTO.getStreet(), clientNewDTO.getNumber(),
                clientNewDTO.getComplement(), clientNewDTO.getDistrict(), clientNewDTO.getPostCode(), client, city);

        client.getAdresses().add(address);
        client.getPhoneNumbers().add(clientNewDTO.getPhoneNumber());

        if(clientNewDTO.getLandline() != null){
            client.getPhoneNumbers().add(clientNewDTO.getLandline());
        }

        if(clientNewDTO.getSecondPhoneNumber() != null){
            client.getPhoneNumbers().add(clientNewDTO.getSecondPhoneNumber());
        }
        return client;
    }



    private void updateData(Client newClient, Client client) {
        newClient.setName(client.getName());
        newClient.setEmail(client.getEmail());
    }
}
