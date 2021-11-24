package br.com.alison.purchases.repository;

import br.com.alison.purchases.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Transactional
    Client findByEmail(String email);
}
