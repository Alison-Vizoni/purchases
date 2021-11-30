package br.com.alison.purchases.repository;

import br.com.alison.purchases.domain.Client;
import br.com.alison.purchases.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Transactional
    Page<Order> findByClient(Client client, Pageable pageableRequest);
}
