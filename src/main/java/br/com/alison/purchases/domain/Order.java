package br.com.alison.purchases.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date instant;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "order")
    private Payment payment;

//    @ManyToMany
//    @JoinTable(name = "order_product",
//            joinColumns = @JoinColumn(name = "id_order"),
//            inverseJoinColumns = @JoinColumn(name = "id_product"))
//    private List<Product> products = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_delivery_address")
    private Address deliveryAddress;

    public Order() {
    }

    public Order(Long id, Date instant, Payment payment, Client client, Address deliveryAddress) {
        this.id = id;
        this.instant = instant;
        this.payment = payment;
        this.client = client;
        this.deliveryAddress = deliveryAddress;
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

//    public List<Product> getProducts() {
//        return products;
//    }
//
//    public void setProducts(List<Product> products) {
//        this.products = products;
//    }

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
}
