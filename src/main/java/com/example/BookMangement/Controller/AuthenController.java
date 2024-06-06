package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Entity.Role;
import com.example.BookMangement.Entity.User;
import com.example.BookMangement.Repository.RoleRepository;
import com.example.BookMangement.Repository.TicketDetailRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.BookService;
import com.example.BookMangement.Service.MemberService;
import com.example.BookMangement.Service.TicketService;
import com.example.BookMangement.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author xuanl
 * @version 01-00
 * @since 5/08/2024
 */
@Controller
public class AuthenController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private BookService bookService;
    @Autowired
    private TicketDetailRepository ticketDetailRepository;

    // Index page
    @GetMapping("/")
    public String indexPage(HttpSession session, Model model) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("totalMember",memberService.countMember());
        model.addAttribute("totalTicket",ticketService.getTotalTickets());
        model.addAttribute("totalBooks",bookService.getTotalBooks());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        List<Object[]> results = ticketDetailRepository.findTopRentedBooks(PageRequest.of(0, 5));
        List<BookInfo> topRentedBooks = new ArrayList<>();
        for (Object[] result : results) {
            Book book = (Book) result[0];
            Long rentCount = (Long) result[1];
            topRentedBooks.add(new BookInfo(book, rentCount));
        }
        model.addAttribute("topRentedBooks", topRentedBooks);
        return "index";
    }
    // Not have permission page
    @GetMapping("/403")
    public String Page403(HttpSession session, Model model) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        return "Error/403";
    }

    // Login page
    @GetMapping("/login")
    public String loginPage(Principal principal) {
        boolean isAuthenticated = principal != null;
        if (isAuthenticated) {
            return "redirect:/";
        }
        return "Authen/login";
    }
    public static class BookInfo {
        private Book book;
        private Long rentCount;

        public BookInfo(Book book, Long rentCount) {
            this.book = book;
            this.rentCount = rentCount;
        }

        public Book getBook() {
            return book;
        }

        public void setBook(Book book) {
            this.book = book;
        }

        public Long getRentCount() {
            return rentCount;
        }

        public void setRentCount(Long rentCount) {
            this.rentCount = rentCount;
        }
    }




}
