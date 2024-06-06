package com.example.BookMangement.Service;

import com.example.BookMangement.Entity.HistoryChange;
import com.example.BookMangement.Entity.User;

import java.util.Optional;

/**
 * HistoryChangeService
 *
 * @author xuanl
 * @version 01-00
 * @since 6/3/2024
 */
public interface HistoryChangeService {
    public HistoryChange getHistoryChangeById(Long id);

}
