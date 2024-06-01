package com.example.BookMangement.Controller.RestController;

import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Entity.SendRequest;
import com.example.BookMangement.Repository.BookRepository;
import com.example.BookMangement.Repository.SendRequestRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

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

    @GetMapping("/updateRequestStatus")
    public void updateRequestStatus(HttpSession session, Model model, @RequestParam("requestId") Long id, @RequestParam("status") String status) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        SendRequest sendRequest = sendRequestRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid request id: " + id));
        sendRequest.setStatus(status);
        sendRequest.setUpdateDate(LocalDate.now());
        sendRequest.setUpdateBy(name);
        sendRequest.setStatus(status);
        Long productId = sendRequest.getBookId();
        Book book= bookRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid book id: " + id));
        book.setRequest(status);
        bookRepository.save(book);

        sendRequestRepository.save(sendRequest);

    }

}
