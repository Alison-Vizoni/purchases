package br.com.alison.purchases.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "order_table")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date instant;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "order")
    private Payment payment;

    @OneToMany(mappedBy = "id.order")
    private Set<OrderItem> items = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_delivery_address")
    private Address deliveryAddress;

    public Order() {
    }

    public Order(Long id, Date instant, Client client, Address deliveryAddress) {
        this.id = id;
        this.instant = instant;
        this.client = client;
        this.deliveryAddress = deliveryAddress;
    }

    public double getTotalValue(){
        return items.stream().mapToDouble(OrderItem::getSubTotal).sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getInstant() {
        return instant;
    }

    public void setInstant(Date instant) {
        this.instant = instant;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuilder message = new StringBuilder();
        message.append("Order number: ");
        message.append(getId());
        message.append(", instant: ");
        message.append(simpleDateFormat.format(getInstant()));
        message.append(", Client: ");
        message.append(getClient().getName());
        message.append(", payment situation: ");
        message.append(getPayment().getStatus().getDescription());
        message.append("\n");
        for (OrderItem orderItem: getItems()) {
            message.append(orderItem.toString());
        }
        message.append("Total value: ");
        message.append(numberFormat.format(getTotalValue()));
        return message.toString();
    }
}
