package com.example.BookMangement.Service;

import com.example.BookMangement.Entity.BaseRes;
import com.example.BookMangement.Entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;

/**
 * MemberService
 *
 * @author benvo
 * @version 01-00
 * @since 5/15/2024
 */
public interface MemberService {
    public void save(User user);
    public int countMember( );

    BaseRes getCmbSong(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}
