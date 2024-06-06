package com.example.BookMangement.Service;

import com.example.BookMangement.Entity.Author;
import com.example.BookMangement.Entity.BaseRes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * AuthorService
 *
 * @author xuanl
 * @version 01-00
 * @since 5/13/2024
 */

public interface AuthorService {


    public Page<Author> findPaginatedAuthor(int pageNo, int pageSize, String sortField, String sortDirection);

    public Page<Author> findPaginatedKeywordAuthor(int pageNo, int pageSize, String sortField, String sortDirection,String keyword);
    public Author getAuthorById(Long id) ;

    public List<Author> searchAuthor(String keyword) ;


    BaseRes getCmbSong(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}
