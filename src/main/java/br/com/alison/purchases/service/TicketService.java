package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.TicketPayment;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class TicketService {

    public static void addDueDate(TicketPayment ticketPayment, Date orderInstant){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(orderInstant);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        ticketPayment.setDueDate(calendar.getTime());
    }
}
