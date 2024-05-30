package com.example.BookMangement.Service;

import com.example.BookMangement.Entity.Ticket;
import com.example.BookMangement.Entity.TicketDetail;

import java.util.List;

/**
 * TicketDetailService
 *
 * @author xuanl
 * @version 01-00
 * @since 5/22/2024
 */
public interface TicketDetailService {

    public List<TicketDetail> getTicketDetailsByBooking(Ticket ticket) ;

}
