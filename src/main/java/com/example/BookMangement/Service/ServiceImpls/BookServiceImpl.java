package com.example.BookMangement.Service.ServiceImpls;

import com.example.BookMangement.Entity.BaseRes;
import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Repository.BookRepository;
import com.example.BookMangement.Service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * BookServiceImpl
 *
 * @author xuanl
 * @version 01-00
 * @since 5/14/2024
 */
@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;


    @Override
    public Book getBookById(Long id) {
        try {
            Optional<Book> book = bookRepository.findById(id);
            return book.orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        } catch (Exception ex) {
            log.error("BookServiceImpl_getBookById_Error :", ex);
            return null;
        }
    }

    @Override
    public List<Book> searchBook(String keyword) {
        if(keyword!=null){
            return bookRepository.findAll(keyword);
        }
        return bookRepository.findAll();
    }

    @Override
    public long getTotalBooks() {
        return bookRepository.count();
    }



    @Override
    public BaseRes getCmbSong(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Page<Book> ticketPage;

        ticketPage = bookRepository.findAllByKeyword(keyword, pageable);


        BaseRes baseRes = new BaseRes();
        baseRes.setStatus("success");
        baseRes.setCode(200);

        if (ticketPage.getTotalElements() == 0) {
            baseRes.setMessage("No records found");
        } else {
            baseRes.setMessage("Successful");
        }

        baseRes.setData(ticketPage);

        return baseRes;
    }
}
