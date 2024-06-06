package com.example.BookMangement.Controller.RestController;

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
import com.example.BookMangement.Service.HistoryChangeService;
import com.example.BookMangement.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * RequestRestController
 *
 * @author xuanl
 * @version 01-00
 * @since 5/31/2024
 */
@RestController
@RequestMapping("/api/v1/send-request")
public class RequestRestController {
    @Autowired
    private SendRequestRepository sendRequestRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private HistoryChangeRepository historyChangeRepository;
    @Autowired
    private HistoryChangeService historyChangeService;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    @GetMapping("/updateRequestStatus")
    public void updateRequestStatus(HttpSession session, Model model, @RequestParam("requestId") Long id, @RequestParam("status") String status) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);

        SendRequest sendRequest = sendRequestRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid request id: " + id));
        sendRequest.setStatus(status);
        sendRequest.setUpdateDate(LocalDate.now());
        sendRequest.setUpdateBy(name);

        Long bookId = sendRequest.getBookId();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Invalid book id: " + bookId));
        book.setRequest(status);
        if(Objects.equals(status, "4")){
            List<HistoryChange> changes = historyChangeRepository.findByRequestId(id);
            applyChanges(book, changes);
            bookRepository.save(book);
        }

        bookRepository.save(book);
        sendRequestRepository.save(sendRequest);
    }

    public void applyChanges(Book book, List<HistoryChange> changes) {
        for (HistoryChange change : changes) {
            switch (change.getFieldName()) {
                case "title":
                    book.setTitle(change.getNewValue());
                    break;
                case "price":
                    book.setPrice(Double.parseDouble(change.getNewValue()));
                    break;
                case "publishYear":
                    book.setPublishYear(Integer.parseInt(change.getNewValue()));
                    break;
                case "quantity":
                    book.setQuantity(Integer.parseInt(change.getNewValue()));
                    break;
                case "authors":
                    List<String> authorNames = Arrays.asList(change.getNewValue().split(", "));
                    Set<Author> newAuthors = authorRepository.findByNameIn(authorNames);
                    book.setAuthors(newAuthors);
                    break;
                case "categories":
                    List<String> categoryTitles = Arrays.asList(change.getNewValue().split(", "));
                    Set<BookCategory> newCategories = bookCategoryRepository.findByTitleIn(categoryTitles);
                    book.setBookCategories(newCategories);
                    break;
                default:
                    break;
            }
        }
        // Sử dụng giá trị từ thay đổi cuối cùng
        if (!changes.isEmpty()) {
            HistoryChange lastChange = changes.get(changes.size() - 1);
            book.setUpdateDate(lastChange.getUpdateDate());
            book.setUpdateBy(lastChange.getUpdateBy());
        }
    }


}
