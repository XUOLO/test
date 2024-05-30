package com.example.BookMangement.Service;


import com.example.BookMangement.Entity.TicketItem;

import java.util.Collection;


public interface ListTicketService {

    public void add(TicketItem newTicketItem);

    public void remove(long id);


    public TicketItem update(long productId, int quantity);

    public void clear();
    public Collection<TicketItem> getAllListTicketItem();

    public int getCount();

    public double getAmount();

}
