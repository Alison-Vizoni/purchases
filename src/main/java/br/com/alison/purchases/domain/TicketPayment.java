package br.com.alison.purchases.domain;

import br.com.alison.purchases.domain.enums.PaymentStatus;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class TicketPayment extends Payment{

    private Date dueDate;
    private Date paymentDate;

    public TicketPayment() {
    }

    public TicketPayment(Long id, PaymentStatus status, Order payment, Date dueDate, Date paymentDate) {
        super(id, status, payment);
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
