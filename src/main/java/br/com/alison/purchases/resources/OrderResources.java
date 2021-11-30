package br.com.alison.purchases.resources;

import br.com.alison.purchases.domain.Category;
import br.com.alison.purchases.domain.Order;
import br.com.alison.purchases.dto.CategoryDTO;
import br.com.alison.purchases.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/orders")
public class OrderResources {

    @Autowired
    private OrderService service;

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id){
        Order order = service.findOrderById(id);
        return ResponseEntity.ok().body(order);
    }

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody Order order){
        order = service.insert(order);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping()
    public ResponseEntity<Page<Order>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "instant") String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction){
        Page<Order> categories = service.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok().body(categories);
    }

}
