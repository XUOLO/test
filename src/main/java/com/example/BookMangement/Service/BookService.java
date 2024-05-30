package com.example.BookMangement.Service;

import com.example.BookMangement.Entity.BaseRes;
import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Entity.BookCategory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * BookService
 *
 * @author xuanl
 * @version 01-00
 * @since 5/10/2024
 */

public interface BookService {


    public Book getBookById(Long id) ;

    public List<Book> searchBook(String keyword);
    public long getTotalBooks();

    public BaseRes getCmbSong(String keySearch, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

}
