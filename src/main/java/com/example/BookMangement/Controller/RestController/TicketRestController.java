package com.example.BookMangement.Controller.RestController;

import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Entity.Ticket;
import com.example.BookMangement.Entity.TicketDetail;
import com.example.BookMangement.Entity.TicketItem;
import com.example.BookMangement.Repository.BookRepository;
import com.example.BookMangement.Repository.TicketDetailRepository;
import com.example.BookMangement.Repository.TicketRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.BookService;
import com.example.BookMangement.Service.ListTicketService;
import com.example.BookMangement.Service.MemberService;
import com.example.BookMangement.Service.TicketDetailService;
import com.example.BookMangement.Service.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TicketRestController
 *
 * @author xuanl
 * @version 01-00
 * @since 5/17/2024
 */
@RestController
@RequestMapping("/api/v1/list-ticket")
public class TicketRestController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private ListTicketService listTicketService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketDetailRepository ticketDetailRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketDetailService ticketDetailService;

    @GetMapping("/all-book")
    public List<Book> listAllBook(){
        return bookRepository.findByIsDeleteFalse();
    }


    @GetMapping("/itemCount")
    public int getCartItemCount() {
        int itemCount = listTicketService.getCount();
        return itemCount;
    }
    @GetMapping("/remove/{id}")
    public void removeItem(@PathVariable("id") int id){
         listTicketService.remove(id);
    }

    @GetMapping("/infoMember")
    public int showInfoMember(@RequestParam("memberId") long memberId) {
        return ticketDetailRepository.getTotalQuantityByMemberId(memberId);

    }
    @GetMapping("/totalBookMember")
    public ResponseEntity<Integer> getTotalByMemberId(@RequestParam("memberId") String memberId) {
        try {
            Long memberIdLong = Long.valueOf(memberId);
            int total = ticketService.getTotalByMemberId(memberIdLong);
            return ResponseEntity.ok(total);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/totalBookOverdue")
    public ResponseEntity<Optional<Integer>> getTotalBookOverdueByMemberId(@RequestParam("memberId") String memberId) {
        try {
            Long memberIdLong = Long.valueOf(memberId);
            Optional<Integer> total = ticketService.getTotalBookOverdueByMemberId(memberIdLong);
            return ResponseEntity.ok(total);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/update")
    public void updateItem(@RequestParam("bookId") Integer bookId, @RequestParam("quantity") Integer quantity, HttpServletRequest request) {
        listTicketService.update(bookId, quantity);
    }
    @GetMapping("/listChosen")
    public List<Book> listChosenItem() {
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
        return listChosen;

    }


    @GetMapping("/search")
    public List<Book>  searchItem(@RequestParam("keyword") String keyword) {
    return bookService.searchBook(keyword);
    }

    @GetMapping("/ticket-date")
    public ResponseEntity<Map<String, List<?>>> getAllTicketsByTakeDate() {
        List<Ticket> tickets = ticketRepository.findAll();

        Map<LocalDate, Integer> totalsByTakeDate = new HashMap<>();
        for (Ticket ticket : tickets) {
            LocalDate takeDate = ticket.getTakeDate();
            int total = ticket.getTotal();
            totalsByTakeDate.merge(takeDate, total, Integer::sum);
        }

        List<LocalDate> sortedTakeDates = new ArrayList<>(totalsByTakeDate.keySet());
        sortedTakeDates.sort(Comparator.naturalOrder());

        List<Integer> quantities = new ArrayList<>();
        List<String> takeDates = new ArrayList<>();

        for (LocalDate takeDate : sortedTakeDates) {
            quantities.add(totalsByTakeDate.get(takeDate));
            takeDates.add(takeDate.toString());
        }

        Map<String, List<?>> result = new HashMap<>();
        result.put("quantity", quantities);
        result.put("takeDate", takeDates);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/updateTicketStatus")
    public void updateTicketStatus(HttpSession session, Model model, @RequestParam("ticketId") Long id, @RequestParam("status") String status, HttpServletRequest request) {
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

    }
}
