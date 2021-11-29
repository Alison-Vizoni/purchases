package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.Order;
import br.com.alison.purchases.domain.OrderItem;
import br.com.alison.purchases.domain.TicketPayment;
import br.com.alison.purchases.domain.enums.PaymentStatus;
import br.com.alison.purchases.repository.OrderItemRepository;
import br.com.alison.purchases.repository.OrderRepository;
import br.com.alison.purchases.repository.PaymentRepository;
import br.com.alison.purchases.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repositry;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private EmailService emailService;

    public Order findOrderById(Long id){
        Optional<Order> order = repositry.findById(id);

        return order.orElseThrow(() -> new ObjectNotFoundException(new StringBuilder()
                .append("Object not found! Id: ")
                .append(id)
                .append(", type: ")
                .append(Order.class.getName()).toString()));
    }

    @Transactional
    public Order insert(Order order) {
        order.setId(null);
        order.setInstant(new Date());
        order.setClient(clientService.findClientById(order.getClient().getId()));
        order.getPayment().setStatus(PaymentStatus.PENDING);
        order.getPayment().setOrder(order);

        if(order.getPayment() instanceof TicketPayment){
            TicketPayment ticketPayment = (TicketPayment) order.getPayment();
            TicketService.addDueDate(ticketPayment, order.getInstant());
        }

        order = repositry.save(order);
        paymentRepository.save(order.getPayment());
        for (OrderItem orderItem : order.getItems()) {
            orderItem.setDiscount(0.0);
            orderItem.setPrice(productService.findProductById(orderItem.getProduct().getId()).getPrice());
            orderItem.setOrder(order);
        }
        orderItemRepository.saveAll(order.getItems());
        emailService.sendOrderConfirmationEmail(order);
        return order;
    }
}
