package com.example.BookMangement.Controller.RestController;

import com.example.BookMangement.Entity.BaseRes;
import com.example.BookMangement.Service.AuthorService;
import com.example.BookMangement.Service.BookCategoryService;
import com.example.BookMangement.Service.BookService;
import com.example.BookMangement.Service.MemberService;
import com.example.BookMangement.Service.TicketService;
import com.example.BookMangement.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * PaginattionRestController
 *
 * @author xuanl
 * @version 01-00
 * @since 5/30/2024
 */
@RestController
@RequestMapping("/api/v1/page")
public class PaginationRestController {
    @Autowired
    private BookService bookService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private BookCategoryService bookCategoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;

    @GetMapping("/book")
    public ResponseEntity<BaseRes> getBookList(
            @RequestParam("keyword") String keyword,
            Pageable pageable,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        BaseRes bookList = bookService.getCmbSong(keyword, pageable, httpServletRequest, httpServletResponse);
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }

    @GetMapping("/book-category")
    public ResponseEntity<BaseRes> getBookCategoryList(
            @RequestParam("keyword") String keyword,
            Pageable pageable,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        BaseRes ticketList = bookCategoryService.getCmbSong(keyword, pageable, httpServletRequest, httpServletResponse);
        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }
    @GetMapping("/authors")
    public ResponseEntity<BaseRes> getAuthorList(
            @RequestParam("keyword") String keyword,
            Pageable pageable,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        BaseRes ticketList = authorService.getCmbSong(keyword, pageable, httpServletRequest, httpServletResponse);
        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }
    @GetMapping("/employee")
    public ResponseEntity<BaseRes> getEmployeeList(
            @RequestParam("keyword") String keyword,
            Pageable pageable,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        BaseRes ticketList = userService.getCmbSong(keyword, pageable, httpServletRequest, httpServletResponse);
        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }

    @GetMapping("/member")
    public ResponseEntity<BaseRes> getMemberList(
            @RequestParam("keyword") String keyword,
            Pageable pageable,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        BaseRes ticketList = memberService.getCmbSong(keyword, pageable, httpServletRequest, httpServletResponse);
        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }

    @GetMapping("/tickets")
    public ResponseEntity<BaseRes> getTicketList(
            @RequestParam("keyword") String keyword,
            Pageable pageable,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        BaseRes ticketList = ticketService.getCmbSong(keyword, pageable, httpServletRequest, httpServletResponse);
        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }
}
