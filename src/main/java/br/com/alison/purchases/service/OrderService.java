package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.Order;
import br.com.alison.purchases.repository.OrderRepository;
import br.com.alison.purchases.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repositry;

    public Order findOrderById(Long id){
        Optional<Order> order = repositry.findById(id);

        return order.orElseThrow(() -> new ObjectNotFoundException(new StringBuilder()
                .append("Object not found! Id: ")
                .append(id)
                .append(", type: ")
                .append(Order.class.getName()).toString()));
    }
}
