package br.com.alison.purchases.domain;

import br.com.alison.purchases.domain.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private Long status;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_order")
    @MapsId
    private Order order;

    public Payment() {
    }

    public Payment(Long id, PaymentStatus status, Order order) {
        this.id = id;
        this.status = status.getCode();
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentStatus getStatus() {
        return PaymentStatus.toEnum(status);
    }

    public void setStatus(PaymentStatus status) {
        this.status = status.getCode();
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
