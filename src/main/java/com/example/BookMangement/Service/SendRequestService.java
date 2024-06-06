package com.example.BookMangement.Service;

import com.example.BookMangement.Entity.BaseRes;
import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Entity.SendRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * SendRequestService
 *
 * @author benvo
 * @version 01-00
 * @since 5/31/2024
 */
public interface SendRequestService {
    BaseRes getCmbSong(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpSession session);
    BaseRes getCmbAdmin(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    public SendRequest getSendRequestById(Long id) ;
 }
