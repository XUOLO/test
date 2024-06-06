package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.Author;
import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Entity.BookCategory;
import com.example.BookMangement.Entity.HistoryChange;
import com.example.BookMangement.Entity.SendRequest;
import com.example.BookMangement.Repository.AuthorRepository;
import com.example.BookMangement.Repository.BookCategoryRepository;
import com.example.BookMangement.Repository.BookRepository;
import com.example.BookMangement.Repository.HistoryChangeRepository;
import com.example.BookMangement.Repository.SendRequestRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.BookService;
import com.example.BookMangement.Service.SendRequestService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * BookController
 *
 * @author xuanl
 * @version 01-00
 * @since 5/10/2024
 */
@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookCategoryRepository bookCategoryRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private HistoryChangeRepository historyChangeRepository;
    @Autowired
    private SendRequestService sendRequestService;
    @Autowired
    private SendRequestRepository sendRequestRepository;

    @GetMapping("/new-book")
    public String showNewEmployeeForm(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("book", new Book());
        model.addAttribute("listBookCategories", bookCategoryRepository.findAll());
        model.addAttribute("listAuthors", authorRepository.findAll());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Book/new-book";
    }
    @GetMapping("/employee")
    public String showListBookEmployee(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("listBook", bookRepository.findByIsDeleteFalse());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Book/list-book-employee";
    }
    @GetMapping("/")
    public String showListBook(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("listBook", bookRepository.findByIsDeleteFalse());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Book/list-book";
    }



    @GetMapping("/edit-book/{id}")
    public String showUpdateBookForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        Book book = bookService.getBookById(id);
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        List<Author> authorList = authorRepository.findAll();
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("listBookCategories", bookCategoryRepository.findAll());
        model.addAttribute("listAuthors", authorRepository.findAll());
        model.addAttribute("book", book);
        model.addAttribute("updateDate", book.getUpdateDate());
        model.addAttribute("updateBy", book.getUpdateBy());
        model.addAttribute("authorList", authorList);
        model.addAttribute("bookCategoryList", bookCategoryList);
        String nameLogin = (String) session.getAttribute("name");
        model.addAttribute("name", nameLogin);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);

        return "Book/edit-book";
    }

    @GetMapping("/delete-book/{id}")
    public String deleteEmployee(@PathVariable(value = "id") long id) {
        Book book = bookService.getBookById(id);
        book.setIsDelete(true);
        bookRepository.save(book);
        return "redirect:/book/";
    }


    @PostMapping("/save-book")
    public String saveBook(HttpSession session, @Valid @ModelAttribute("book") Book book, BindingResult bindingResult, RedirectAttributes redirectAttributes,  Model model) {

        if(bindingResult.hasErrors()){
            String name = (String) session.getAttribute("name");
            model.addAttribute("name", name);
            model.addAttribute("listBookCategories", bookCategoryRepository.findAll());
            model.addAttribute("listAuthors", authorRepository.findAll());
            return "Book/new-book";
        }
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        book.setCreateDate(LocalDate.now());
        book.setCreateBy(name);
        book.setUpdateDate(LocalDate.now());
        book.setUpdateBy(name);
        book.setIsDelete(false);
        bookRepository.save(book);
        redirectAttributes.addFlashAttribute("newBookSuccess", "Success add new book !");
        return "redirect:/book/new-book";
    }

    @PostMapping("/edit-book")
    public String editBook(Authentication authentication,Model model,HttpSession session,@Valid @ModelAttribute("book") Book book, BindingResult bindingResult, @RequestParam("bookCategories") List<Long> bookCategoryIds, @RequestParam("authors") List<Long> authorIds, RedirectAttributes redirectAttributes)  {

        if(bindingResult.hasErrors()){
            String name = (String) session.getAttribute("name");
            model.addAttribute("name", name);
            String userRole = authentication.getAuthorities().iterator().next().getAuthority();
            model.addAttribute("userRole", userRole);
            model.addAttribute("bookCategoryList", bookCategoryRepository.findAll());
            model.addAttribute("authorList", authorRepository.findAll());
            model.addAttribute("editBookError", "Fail edit book !");

            return "Book/edit-book";
        }
        String nameLogin = (String) session.getAttribute("name");
        book.setUpdateBy(nameLogin);
        book.setUpdateDate(LocalDate.now());
        book.setIsDelete(book.getIsDelete());
        book.clearBookCategories();
        book.clearAuthors();

        List<BookCategory> bookCategories = bookCategoryRepository.findAllById(bookCategoryIds);
        book.setBookCategories(new HashSet<>(bookCategories));

        List<Author> authors = authorRepository.findAllById(authorIds);
        book.setAuthors(new HashSet<>(authors));
        bookRepository.save(book);
        redirectAttributes.addFlashAttribute("editBookSuccess", "Success edit book !");
        return "redirect:/book/edit-book/" + book.getId();
    }


    @PostMapping("/edit-book-employee")
    public String editBookEmployee(Authentication authentication,Model model,HttpSession session,@Valid @ModelAttribute("book") Book book, BindingResult bindingResult,@RequestParam("requestId") Long requestId, @RequestParam("bookCategories") List<Long> bookCategoryIds, @RequestParam("authors") List<Long> authorIds, RedirectAttributes redirectAttributes)  {

        if(bindingResult.hasErrors()){
            String name = (String) session.getAttribute("name");
            model.addAttribute("name", name);
            String userRole = authentication.getAuthorities().iterator().next().getAuthority();
            model.addAttribute("userRole", userRole);
            model.addAttribute("bookCategoryList", bookCategoryRepository.findAll());
            model.addAttribute("authorList", authorRepository.findAll());
            model.addAttribute("editBookError", "Fail edit book !");

            return "Book/edit-book";
        }
        String nameLogin = (String) session.getAttribute("name");
        Long userId = (Long) session.getAttribute("userId");


        List<BookCategory> bookCategories = bookCategoryRepository.findAllById(bookCategoryIds);
        book.setBookCategories(new HashSet<>(bookCategories));

        List<Author> authors = authorRepository.findAllById(authorIds);
        book.setAuthors(new HashSet<>(authors));


        Book oldBook = bookRepository.findById(book.getId()).orElse(null);
        SendRequest sendRequest = sendRequestRepository.findById(requestId).orElse(null);

        if (!Objects.equals(oldBook.getTitle(), book.getTitle())) {
            HistoryChange hc = new HistoryChange();
            hc.setEntityType("book");
            hc.setFieldName("title");
            hc.setUpdateDate(LocalDate.now());
            hc.setUpdateBy(nameLogin);
            hc.setCreateDate(LocalDate.now());
            hc.setCreateBy(nameLogin);
            hc.setStatus("3");
            hc.setUserId(userId);
            hc.setRequestId(requestId);
            hc.setOldValue(oldBook.getTitle());
            hc.setNewValue(book.getTitle());
            historyChangeRepository.save(hc);
        }

        if (!Objects.equals(oldBook.getPrice(), book.getPrice())) {
            HistoryChange hc = new HistoryChange();
            hc.setEntityType("book");
            hc.setFieldName("price");
            hc.setUpdateDate(LocalDate.now());
            hc.setUpdateBy(nameLogin);
            hc.setCreateDate(LocalDate.now());
            hc.setCreateBy(nameLogin);
            hc.setStatus("3");
            hc.setUserId(userId);
            hc.setRequestId(requestId);
            hc.setOldValue(String.valueOf(oldBook.getPrice()));
            hc.setNewValue(String.valueOf(book.getPrice()));
            historyChangeRepository.save(hc);
        }

        if (!Objects.equals(oldBook.getPublishYear(), book.getPublishYear())) {
            HistoryChange hc = new HistoryChange();
            hc.setEntityType("book");
            hc.setFieldName("publishYear");
            hc.setUpdateDate(LocalDate.now());
            hc.setUpdateBy(nameLogin);
            hc.setCreateDate(LocalDate.now());
            hc.setCreateBy(nameLogin);
            hc.setStatus("3");
            hc.setUserId(userId);
            hc.setRequestId(requestId);
            hc.setOldValue(String.valueOf(oldBook.getPublishYear()));
            hc.setNewValue(String.valueOf(book.getPublishYear()));
            historyChangeRepository.save(hc);
        }

        if (!Objects.equals(oldBook.getQuantity(), book.getQuantity())) {
            HistoryChange hc = new HistoryChange();
            hc.setEntityType("book");
            hc.setFieldName("quantity");
            hc.setUpdateDate(LocalDate.now());
            hc.setUpdateBy(nameLogin);
            hc.setCreateDate(LocalDate.now());
            hc.setCreateBy(nameLogin);
            hc.setStatus("3");
            hc.setUserId(userId);
            hc.setRequestId(requestId);
            hc.setOldValue(String.valueOf(oldBook.getQuantity()));
            hc.setNewValue(String.valueOf(book.getQuantity()));
            historyChangeRepository.save(hc);
        }

        // Kiểm tra các thay đổi trong danh sách tác giả
        Set<Author> oldAuthors = oldBook.getAuthors();
        Set<Author> newAuthors = new HashSet<>(authorRepository.findAllById(authorIds));
        if (!Objects.equals(oldAuthors, newAuthors)) {
            HistoryChange hc = new HistoryChange();
            hc.setEntityType("book");
            hc.setFieldName("authors");
            hc.setUpdateDate(LocalDate.now());
            hc.setUpdateBy(nameLogin);
            hc.setCreateDate(LocalDate.now());
            hc.setCreateBy(nameLogin);
            hc.setStatus("3");
            hc.setUserId(userId);
            hc.setRequestId(requestId);
            hc.setOldValue(oldAuthors.stream().map(Author::getName).collect(Collectors.joining(", ")));
            hc.setNewValue(newAuthors.stream().map(Author::getName).collect(Collectors.joining(", ")));
            historyChangeRepository.save(hc);
        }

        // Kiểm tra các thay đổi trong danh sách danh mục
        Set<BookCategory> oldCategories = oldBook.getBookCategories();
        Set<BookCategory> newCategories = new HashSet<>(bookCategoryRepository.findAllById(bookCategoryIds));
        if (!Objects.equals(oldCategories, newCategories)) {
            HistoryChange hc = new HistoryChange();
            hc.setEntityType("book");
            hc.setFieldName("categories");
            hc.setUpdateDate(LocalDate.now());
            hc.setUpdateBy(nameLogin);
            hc.setCreateDate(LocalDate.now());
            hc.setCreateBy(nameLogin);
            hc.setUserId(userId);
            hc.setStatus("3");
            hc.setRequestId(requestId);
            hc.setOldValue(oldCategories.stream().map(BookCategory::getTitle).collect(Collectors.joining(", ")));
            hc.setNewValue(newCategories.stream().map(BookCategory::getTitle).collect(Collectors.joining(", ")));
            historyChangeRepository.save(hc);
        }


        oldBook.setRequest("3");
        sendRequest.setStatus("3");
        sendRequestRepository.save(sendRequest);
        bookRepository.save(oldBook);
        redirectAttributes.addFlashAttribute("editBookSuccess", "Success edit book !");
        return "redirect:/book/edit-book/" + book.getId();
    }

    @GetMapping("/edit-book-request/{id}")
    public String showEditBookRequestForm(@PathVariable("id") Long id, Model model, HttpSession session) {

        SendRequest sendRequest =sendRequestService.getSendRequestById(id);
        Book book = bookService.getBookById(sendRequest.getBookId());
        model.addAttribute("requestId", id);
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        List<Author> authorList = authorRepository.findAll();
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("listBookCategories", bookCategoryRepository.findAll());
        model.addAttribute("listAuthors", authorRepository.findAll());
        model.addAttribute("book", book);
        model.addAttribute("updateDate", book.getUpdateDate());
        model.addAttribute("updateBy", book.getUpdateBy());
        model.addAttribute("authorList", authorList);
        model.addAttribute("bookCategoryList", bookCategoryList);
        String nameLogin = (String) session.getAttribute("name");
        model.addAttribute("name", nameLogin);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);


        return "Book/edit-book";
    }


    @GetMapping("/detail-book/{id}")
    public String showDetailBook(@PathVariable("id") Long id, Model model, HttpSession session) {


        Book book = bookService.getBookById(id);

        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        List<Author> authorList = authorRepository.findAll();
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("listBookCategories", bookCategoryRepository.findAll());
        model.addAttribute("listAuthors", authorRepository.findAll());
        model.addAttribute("book", book);
        model.addAttribute("updateDate", book.getUpdateDate());
        model.addAttribute("updateBy", book.getUpdateBy());
        model.addAttribute("authorList", authorList);
        model.addAttribute("bookCategoryList", bookCategoryList);
        String nameLogin = (String) session.getAttribute("name");
        model.addAttribute("name", nameLogin);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);


        return "Book/detail-book";
    }

}
