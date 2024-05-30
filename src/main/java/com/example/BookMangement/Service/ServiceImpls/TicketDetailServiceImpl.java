package com.example.BookMangement.Service.ServiceImpls;

import com.example.BookMangement.Entity.Ticket;
import com.example.BookMangement.Entity.TicketDetail;
import com.example.BookMangement.Repository.TicketDetailRepository;
import com.example.BookMangement.Service.TicketDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TicketDetailServiceImpl
 *
 * @author xuanl
 * @version 01-00
 * @since 5/22/2024
 */
@Service
public class TicketDetailServiceImpl implements TicketDetailService {
    @Autowired
    private TicketDetailRepository ticketDetailRepository;
    @Override
    public List<TicketDetail> getTicketDetailsByBooking(Ticket ticket) {
        return ticketDetailRepository.findByTicket(ticket);
    }




}
