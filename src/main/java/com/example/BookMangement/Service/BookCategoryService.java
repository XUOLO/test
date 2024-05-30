package com.example.BookMangement.Service;

import com.example.BookMangement.Entity.BaseRes;
import com.example.BookMangement.Entity.BookCategory;
import com.example.BookMangement.Repository.BookCategoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * BookCategoryService
 *
 * @author benvo
 * @version 01-00
 * @since 5/13/2024
 */

public interface BookCategoryService {

    public BookCategory getBookCategoryById(Long id) ;
    public List<BookCategory> searchBookCategory(String keyword) ;
    public Page<BookCategory> findPaginatedKeywordBookCategory(int pageNo, int pageSize, String sortField, String sortDirection,String keyword);
    public Page<BookCategory> findPaginatedBookCategory(int pageNo, int pageSize, String sortField, String sortDirection);

    BaseRes getCmbSong(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}
