package com.example.BookMangement.Service.ServiceImpls;

import com.example.BookMangement.Entity.HistoryChange;
import com.example.BookMangement.Entity.User;
import com.example.BookMangement.Repository.HistoryChangeRepository;
import com.example.BookMangement.Service.HistoryChangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * HistoryChangeImpl
 *
 * @author xuanl
 * @version 01-00
 * @since 6/3/2024
 */
@Service
@Slf4j
public class HistoryChangeImpl implements HistoryChangeService {
    @Autowired
    private HistoryChangeRepository historyChangeRepository;
    @Override
    public HistoryChange getHistoryChangeById(Long id) {
        try {
            Optional<HistoryChange> user = historyChangeRepository.findById(id);
            return user.orElseThrow(() -> new RuntimeException("HistoryChange not found with id: " + id));
        } catch (Exception ex) {
            log.error("HistoryChangeImpl_getHistoryChangeById_Error :", ex);
            return null;
        }
    }


}
