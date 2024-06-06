package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Entity.HistoryChange;
import com.example.BookMangement.Entity.SendRequest;
import com.example.BookMangement.Repository.BookRepository;
import com.example.BookMangement.Repository.HistoryChangeRepository;
import com.example.BookMangement.Repository.SendRequestRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.BookService;
import com.example.BookMangement.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    private UserService userService;
    @Autowired
    private HistoryChangeRepository historyChangeRepository;


    @GetMapping("/")
    public String listRequests(HttpSession session, Model model) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
         return "Request/list-request";
    }
    @GetMapping("/employee")
    public String listRequestsEmployee(HttpSession session, Model model) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Request/list-request-employee";
    }
    @GetMapping("/new-request")
    public String showRequestForm(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        model.addAttribute("sendRequest", new SendRequest());
        model.addAttribute("bookList", bookRepository.findByIsDeleteFalse());
        return "Request/new-request"; 
    }

    @PostMapping("/save-request")
    public String saveAuthor(Authentication authentication, HttpSession session, @Valid @ModelAttribute("sendRequest") SendRequest sendRequest, @RequestParam("bookId") Long bookId, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            String name = (String) session.getAttribute("name");
            model.addAttribute("name", name);
            String userRole = authentication.getAuthorities().iterator().next().getAuthority();
            model.addAttribute("userRole", userRole);
            model.addAttribute("bookList", bookRepository.findByIsDeleteFalse());
            model.addAttribute("errorRequest", "Fail add new Request !");
            return "Request/new-request";
        }
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Invalid book id: " + bookId));

        if (book.getRequest().equals("1")) {
            String name = (String) session.getAttribute("name");
            model.addAttribute("name", name);
            String userRole = authentication.getAuthorities().iterator().next().getAuthority();
            model.addAttribute("userRole", userRole);
            model.addAttribute("bookList", bookRepository.findByIsDeleteFalse());
            model.addAttribute("errorRequest", "Book already on request !");
            return "Request/new-request";
        }

        model.addAttribute("bookList", bookRepository.findByIsDeleteFalse());
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        sendRequest.setCreateDate(LocalDate.now());
        sendRequest.setCreateBy(name);
        sendRequest.setUpdateDate(LocalDate.now());
        sendRequest.setUpdateBy(name);
        sendRequest.setIsDelete(false);
        sendRequest.setStatus("1");
        sendRequest.setBookId(bookId);
        Long userId = (Long) session.getAttribute("userId");

        sendRequest.setUserId(userId);
        sendRequestRepository.save(sendRequest);
        model.addAttribute("newsendRequestSuccess", "Success add new sendRequest!");
        return "Request/new-request";
    }
    @GetMapping("/history/{id}")
    public String viewRequestHistory(@PathVariable("id") Long requestId, HttpSession session, Model model) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);

        List<HistoryChange> historyChanges = historyChangeRepository.findByRequestId(requestId);
        model.addAttribute("historyChanges", historyChanges);

        return "Request/history-request";
    }


}
