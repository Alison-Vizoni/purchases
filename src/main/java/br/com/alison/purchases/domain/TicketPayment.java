package br.com.alison.purchases.domain;

import br.com.alison.purchases.domain.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table
public class TicketPayment extends Payment{

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dueDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date paymentDate;

    public TicketPayment() {
    }

    public TicketPayment(Long id, PaymentStatus status, Order order, Date dueDate, Date paymentDate) {
        super(id, status, order);
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
