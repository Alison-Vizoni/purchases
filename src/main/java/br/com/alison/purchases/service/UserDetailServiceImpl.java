package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.Client;
import br.com.alison.purchases.repository.ClientRepository;
import br.com.alison.purchases.security.UserSpringSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(email);
        if(client == null){
            throw new UsernameNotFoundException(email);
        }
        return new UserSpringSecurity(client.getId(), client.getEmail(), client.getPassword(), client.getProfiles());
    }
}
