package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Entity.BookCategory;
import com.example.BookMangement.Entity.Ticket;
import com.example.BookMangement.Entity.TicketDetail;
import com.example.BookMangement.Entity.TicketItem;
import com.example.BookMangement.Entity.User;
import com.example.BookMangement.Repository.BookRepository;
import com.example.BookMangement.Repository.TicketDetailRepository;
import com.example.BookMangement.Repository.TicketRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.BookService;
import com.example.BookMangement.Service.ListTicketService;
import com.example.BookMangement.Service.TicketDetailService;
import com.example.BookMangement.Service.TicketService;
import com.example.BookMangement.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * TicketController
 *
 * @author xuanl
 * @version 01-00
 * @since 5/17/2024
 */
@Controller
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ListTicketService listTicketService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketDetailRepository ticketDetailRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketDetailService ticketDetailService;


    @GetMapping("/new-ticket")
    public String showNewTicket(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("listBook", bookRepository.findByIsDeleteFalse());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        int itemCount = listTicketService.getCount();
        model.addAttribute("itemCount", itemCount);
        model.addAttribute("listMember", userRepository.findMemberByIsDeleteFalse());

        List<Book> allBooks = bookRepository.findByIsDeleteFalse();
        for (Book book : allBooks) {
            TicketItem ticketItem = new TicketItem();
            ticketItem.setId(book.getId());
            ticketItem.setTitle(book.getTitle());
            ticketItem.setImage(book.getImage());
            ticketItem.setPublishYear(book.getPublishYear());
            ticketItem.setPrice(book.getPrice());
            ticketItem.setAvailableQuantity(book.getQuantity());
            ticketItem.setAuthors(book.getAuthors());
            ticketItem.setBookCategories(book.getBookCategories());
            listTicketService.add(ticketItem);
        }
        Collection<TicketItem> allTicketItem = listTicketService.getAllListTicketItem();
        model.addAttribute("allTicketItem", allTicketItem);
        List<Book> listChosen = new ArrayList<>();

        Collection<TicketItem> ticketItems = listTicketService.getAllListTicketItem();
        for (TicketItem ticketItem : ticketItems) {
            if (ticketItem.getId() != null) {
                Book book = bookRepository.findById(ticketItem.getId()).orElse(null);
                if (book != null && ticketItem.getQuantity() > 0) {
                    listChosen.add(book);
                }
            }
        }
        model.addAttribute("listChosen", listChosen);
        return "Ticket/new-ticket";
    }

    @GetMapping("/")
    public String showListTicket(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        List<Ticket> tickets = ticketRepository.findByIsDeleteFalse();
        List<Ticket> sortedTickets = tickets.stream()
                .sorted(Comparator.comparing(Ticket::getTakeDate))
                .collect(Collectors.toList());
        model.addAttribute("listTicket", sortedTickets);

        LocalDate today = LocalDate.now();
         for (Ticket ticket : tickets) {
            if (ticket.getReturnDate().isBefore(today) && ticket.getStatus().equals("1")||ticket.getReturnDate().isBefore(today) && ticket.getStatus().equals("3")) {
                ticket.setStatus("3");
                LocalDate returnDate = ticket.getReturnDate();
                long daysBetween = ChronoUnit.DAYS.between(returnDate, today);
                ticket.setOverdueAmount(daysBetween*0.25*ticket.getTotal());
                ticketRepository.save(ticket);

            }
            ticketRepository.save(ticket);
        }
        listTicketService.clear();
        return "Ticket/list-ticket";
    }

    @GetMapping("/clear")
    public String clearItem(HttpServletRequest request) {
        listTicketService.clear();
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/search")
    public List<Book> searchItem(@RequestParam("keyword") String keyword) {

        return bookService.searchBook(keyword);
    }

    @PostMapping("/save-ticket")
    public String submitTicket(@RequestParam("note") String note,
                               @RequestParam("returnDate") LocalDate returnDate,
                               @RequestParam("memberId") Long memberId,
                               @ModelAttribute("ticket") Ticket ticket, Model model,
                               HttpSession session, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        model.addAttribute("listBook", bookRepository.findByIsDeleteFalse());
        int itemCount = listTicketService.getCount();
        model.addAttribute("itemCount", itemCount);
        model.addAttribute("listMember", userRepository.findMemberByIsDeleteFalse());
        Collection<TicketItem> allTicketItem = listTicketService.getAllListTicketItem();
        model.addAttribute("allTicketItem", allTicketItem);
        model.addAttribute("totalAmount", listTicketService.getAmount());
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        List<String> errorMessages = new ArrayList<>();

        Collection<TicketItem> allListTicketItem = listTicketService.getAllListTicketItem();

        for (TicketItem reservationItem : allListTicketItem) {
            if (reservationItem.getId() != null) {
                Long productId = reservationItem.getId();
                int quantityToOrder = reservationItem.getQuantity();

                Book product = bookRepository.findById(productId).orElse(null);
                if (product != null) {
                    int availableQuantity = product.getQuantity();
                    if (quantityToOrder > availableQuantity) {
                        String errorMessage = "Book '" + product.getTitle() + "' not enough quantity.";
                        errorMessages.add(errorMessage);
                    }
                }
            }
        }

        if (!errorMessages.isEmpty()) {
            model.addAttribute("errorMessages", errorMessages);
            return "Error/ErrorPage";
        }

        List<Book> listChosen = new ArrayList<>();
        for (TicketItem ticketItem : allListTicketItem) {
            if (ticketItem.getId() != null) {
                Book book = bookRepository.findById(ticketItem.getId()).orElse(null);
                if (book != null && ticketItem.getQuantity() > 0) {
                    listChosen.add(book);
                }
            }
        }
        User member = userService.getUserById(memberId);

        ticket.setCreateBy(name);
        ticket.setUpdateBy(name);
        ticket.setTakeDate(LocalDate.now());
        ticket.setIsDelete(false);
        ticket.setReturnDate(returnDate);
        ticket.setNote(note);
        ticket.setMember(member);
        ticket.setUpdateDate(LocalDate.now());
        ticket.setTotalAmount(listTicketService.getAmount());
        ticket.setCode("TK" + ticketRepository.count());
        LocalDate today = LocalDate.now();
        if (returnDate.isBefore(today)) {
            if (ticket.getStatus().equals("1")) {
                ticket.setStatus("3");
            }
        } else {
            ticket.setStatus("1");

            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            long delayInDays = ChronoUnit.DAYS.between(today, returnDate);
            executor.schedule(() -> {
                if (ticket.getStatus().equals("1")) {
                    ticket.setStatus("3");
                    ticketRepository.save(ticket);
                }
            }, delayInDays, TimeUnit.DAYS);
        }

        ticketRepository.save(ticket);


        for (TicketItem ticketItem : allListTicketItem) {
            if (ticketItem.getId() != null) {
                Long productId = ticketItem.getId();
                int quantityToOrder = ticketItem.getQuantity();

                Book product = bookRepository.findById(productId).orElse(null);
                if (product != null) {
                    int availableQuantity = product.getQuantity();
                    int updatedQuantity = availableQuantity - quantityToOrder;
                    product.setQuantity(updatedQuantity);

                    bookRepository.save(product);
                }
            }
        }

        for (TicketItem ticketItem : allListTicketItem) {
            if (ticketItem.getId() != null) {
                Book product = bookRepository.findById(ticketItem.getId()).orElse(null);
                if (product != null && ticketItem.getQuantity() > 0) {
                    TicketDetail bookingDetail = new TicketDetail();
                    bookingDetail.setTicket(ticket);
                    bookingDetail.setBook(product);
                    bookingDetail.setPrice(product.getPrice());
                    bookingDetail.setQuantity(ticketItem.getQuantity());
                    bookingDetail.setTotal(0.5 * ticketItem.getQuantity());
                    ticketDetailRepository.save(bookingDetail);
                }
            }
        }

        // Tính tổng số sách trong TicketDetail
        int totalBookCount = 0;
        List<TicketDetail> ticketDetails = ticketDetailRepository.findByTicket(ticket);
        for (TicketDetail ticketDetail : ticketDetails) {
            totalBookCount += ticketDetail.getQuantity();
        }

        // Gán tổng số sách vào ticket.total
        ticket.setTotal(totalBookCount);
        ticketRepository.save(ticket);

        listTicketService.clear();

        redirectAttributes.addFlashAttribute("successMessages", "Success add new ticket !");
        return "redirect:/ticket/new-ticket";
    }

    @GetMapping("/detail/borrows-book/{id}")
    public String showBorrowsDetail(Authentication authentication, HttpSession session, @PathVariable(value = "id") long memberId, Model model) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        List<Ticket> memberBorrows = ticketService.getAllTicketsByMemberId(memberId);
        User memberName = userService.getUserById(memberId);
        model.addAttribute("memberName", memberName.getName());

        model.addAttribute("memberBorrows", memberBorrows);
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Ticket/detail-borrows-book";
    }

    @GetMapping("/detail-ticket/{id}")
    public String showTicketDetail(HttpSession session, @PathVariable(value = "id") long id, Model model) {
        Ticket ticket = ticketService.getTicketById(id);
        model.addAttribute("ticket", ticket);
        model.addAttribute("ticketId", ticket.getId());
        model.addAttribute("memberName", ticket.getMember().getName());
        model.addAttribute("totalAmount", ticket.getTotalAmount());
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        List<TicketDetail> ticketDetails = ticketDetailService.getTicketDetailsByBooking(ticket);
        model.addAttribute("ticketDetails", ticketDetails);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Ticket/detail-ticket";
    }

    @PostMapping("/updateTicketStatus/{id}")
    public String updateTicketStatus(HttpSession session, Model model, @PathVariable("id") Long id, @RequestParam("status") String status, HttpServletRequest request) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid order id: " + id));
        ticket.setStatus(status);
        ticket.setUpdateDate(LocalDate.now());
        ticket.setUpdateBy(name);

        List<TicketDetail> ticketDetails = ticketDetailService.getTicketDetailsByBooking(ticket);
        for (TicketDetail ticketDetail : ticketDetails) {
            if (ticketDetail.getId() != null) {
                Long bookId = ticketDetail.getBook().getId();
                int quantityToOrder = (int) ticketDetail.getQuantity();

                Book product = bookRepository.findById(bookId).orElse(null);
                if (product != null) {
                    int availableQuantity = product.getQuantity();
                    int updatedQuantity = availableQuantity + quantityToOrder;
                    product.setQuantity(updatedQuantity);
                    bookRepository.save(product);
                }
            }
        }

        ticketRepository.save(ticket);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/delete-ticket/{id}")
    public String deleteTicket(@PathVariable(value = "id") long id) {
        Ticket ticket = ticketService.getTicketById(id);
        ticket.setIsDelete(true);
        ticketRepository.save(ticket);
        return "redirect:/ticket/";
    }


}
