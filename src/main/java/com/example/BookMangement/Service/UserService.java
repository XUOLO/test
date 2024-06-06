package com.example.BookMangement.Service;

import com.example.BookMangement.Entity.BaseRes;
import com.example.BookMangement.Entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * fff
 *
 * @author xuanl
 * @version 01-00
 * @since 5/08/2024
 */

public interface UserService {

    public void save(User user);
    public User findByUsername(String username);
    public User getUserById(Long id);
    public Page<User> findPaginatedEmployee(int pageNo, int pageSize, String sortField, String sortDirection);

    BaseRes getCmbSong(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    User viewById(long userId);
}
