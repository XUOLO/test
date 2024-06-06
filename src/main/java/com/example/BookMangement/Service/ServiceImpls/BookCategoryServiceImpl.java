package com.example.BookMangement.Service.ServiceImpls;


import com.example.BookMangement.Entity.BaseRes;
import com.example.BookMangement.Entity.BookCategory;
import com.example.BookMangement.Repository.BookCategoryRepository;
import com.example.BookMangement.Service.BookCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * BookCategoryServiceImpls
 *
 * @author benvo
 * @version 01-00
 * @since 5/14/2024
 */
@Service
@Slf4j
public class BookCategoryServiceImpl implements BookCategoryService {
    @Autowired
    private BookCategoryRepository bookCategoryRepository;


    @Override
    public BookCategory getBookCategoryById(Long id) {

        try {
            Optional<BookCategory> bookCategory = bookCategoryRepository.findById(id);
            return bookCategory.orElseThrow(() -> new RuntimeException("Book category not found with id: " + id));
        } catch (Exception ex) {
            log.error("BookCategoryServiceImpl_getBookCategoryById_Error :", ex);
            return null;
        }
    }
    @Override
    public List<BookCategory> searchBookCategory(String keyword) {

        if(keyword!=null){
            return bookCategoryRepository.findAll(keyword);
        }
        return bookCategoryRepository.findAll();
    }
    @Override
    public Page<BookCategory> findPaginatedKeywordBookCategory(int pageNo, int pageSize, String sortField, String sortDirection, String keyword){
        Sort sort= sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        Pageable pageable= PageRequest.of(pageNo - 1,pageSize,sort);
        return this.bookCategoryRepository.findByTitleAndIsDeleteFalse(keyword,pageable);
    }
    @Override
    public Page<BookCategory> findPaginatedBookCategory(int pageNo, int pageSize, String sortField, String sortDirection){
        Sort sort= sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        Pageable pageable= PageRequest.of(pageNo - 1,pageSize,sort);
        return this.bookCategoryRepository.findAllByIsDeleteFalse(pageable);
    }

    @Override
    public BaseRes getCmbSong(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Page<BookCategory> bookCategoryPage;

        bookCategoryPage = bookCategoryRepository.findAllByKeyword(keyword, pageable);

        BaseRes baseRes = new BaseRes();
        baseRes.setStatus("success");
        baseRes.setCode(200);

        if (bookCategoryPage.getTotalElements() == 0) {
            baseRes.setMessage("No records found");
        } else {
            baseRes.setMessage("Successful");
        }

        baseRes.setData(bookCategoryPage);

        return baseRes;
    }
}
