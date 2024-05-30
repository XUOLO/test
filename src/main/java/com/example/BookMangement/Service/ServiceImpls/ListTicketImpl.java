package com.example.BookMangement.Service.ServiceImpls;

import com.example.BookMangement.Entity.TicketItem;
import com.example.BookMangement.Service.ListTicketService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * ListTicketImpl
 *
 * @author xuanl
 * @version 01-00
 * @since 5/17/2024
 */
@Service
public class ListTicketImpl implements ListTicketService {

    Map<Long, TicketItem> ticketItemMap = new HashMap<>();

    @Override
    public void add(TicketItem newTicketItem) {
        TicketItem cartItem = ticketItemMap.get(newTicketItem.getId());
        if (cartItem == null) {
            newTicketItem.setQuantity(0);
            ticketItemMap.put(newTicketItem.getId(), newTicketItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity());
        }
    }

    @Override
    public void remove(long id) {
        ticketItemMap.remove(id);
    }




    @Override
    public TicketItem update(long bookId, int quantity) {
        TicketItem ticketItem = ticketItemMap.get(bookId);
        if (ticketItem != null) {
            ticketItem.setQuantity(quantity);
        }
        return ticketItem;
    }

    @Override
    public void clear() {
        ticketItemMap.clear();
    }

    @Override
    public Collection<TicketItem> getAllListTicketItem() {
        return ticketItemMap.values();
    }

    @Override
    public int getCount() {
        return ticketItemMap.values().size();
    }

    @Override
    public double getAmount() {
        return ticketItemMap.values().stream()
                .mapToDouble(item->item.getQuantity()*0.5).sum();
    }
}
