package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.SendRequest;
import com.example.BookMangement.Repository.BookRepository;
import com.example.BookMangement.Repository.SendRequestRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.BookService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/**
 * SendRequest
 *
 * @author benvo
 * @version 01-00
 * @since 5/31/2024
 */
@Controller
@RequestMapping("/send-request")
public class SendRequestController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SendRequestRepository sendRequestRepository;


    @GetMapping("/")
    public String listRequests(Model model) {
        //model.addAttribute("requests", bookDeletionRequestService.getAllRequests());
        model.addAttribute("books", bookRepository.findByIsDeleteFalse());
        return "Request/list-request"; // View name for the list request page
    }
    @GetMapping("/new-request")
    public String showRequestForm(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("sendRequest", new SendRequest());
        model.addAttribute("bookList", bookRepository.findByIsDeleteFalse());
        return "Request/new-request"; // View name for the new request form
    }

    @PostMapping("/save-request")
    public String saveAuthor(Authentication authentication, HttpSession session, @Valid @ModelAttribute("sendRequest") SendRequest sendRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            String name = (String) session.getAttribute("name");
            model.addAttribute("name", name);
            String userRole = authentication.getAuthorities().iterator().next().getAuthority();
            model.addAttribute("userRole", userRole);
            model.addAttribute("bookList", bookRepository.findByIsDeleteFalse());
            redirectAttributes.addFlashAttribute("errorRequest", "Fail add new Request !");
            return "Request/new-request";
        }

        model.addAttribute("bookList", bookRepository.findByIsDeleteFalse());
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        sendRequest.setCreateDate(LocalDate.now());
        sendRequest.setCreateBy(name);
        sendRequest.setUpdateDate(LocalDate.now());
        sendRequest.setUpdateBy(name);
        sendRequest.setIsDelete(false);
        sendRequest.setStatus("1");
        sendRequestRepository.save(sendRequest);
        model.addAttribute("newsendRequestSuccess", "Success add new sendRequest!");
        return "Request/new-request";
    }

}
