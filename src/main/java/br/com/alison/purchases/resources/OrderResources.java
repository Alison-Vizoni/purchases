package br.com.alison.purchases.resources;

import br.com.alison.purchases.domain.Order;
import br.com.alison.purchases.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/orders")
public class OrderResources {

    @Autowired
    private OrderService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Order order = service.findOrderById(id);
        return ResponseEntity.ok().body(order);
    }
}
