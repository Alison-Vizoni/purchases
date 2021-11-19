package br.com.alison.purchases.domain;

import br.com.alison.purchases.domain.enums.PaymentStatus;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class CardPayment extends Payment {

    private Integer installmentsNumber;

    public CardPayment() {
    }

    public CardPayment(Long id, PaymentStatus status, Order payment, Integer installmentsNumber) {
        super(id, status, payment);
        this.installmentsNumber = installmentsNumber;
    }

    public Integer getInstallmentsNumber() {
        return installmentsNumber;
    }

    public void setInstallmentsNumber(Integer installmentsNumber) {
        this.installmentsNumber = installmentsNumber;
    }
}
