package com.example.BookMangement.Service;

import com.example.BookMangement.Entity.BaseRes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;

/**
 * SendRequestService
 *
 * @author benvo
 * @version 01-00
 * @since 5/31/2024
 */
public interface SendRequestService {
    BaseRes getCmbSong(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}
