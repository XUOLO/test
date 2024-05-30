package com.example.BookMangement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Ticket
 *
 * @author xuanl
 * @version 01-00
 * @since 5/17/2024
 */
@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "202403_tickets")
public class Ticket {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_book")
    private int total;
    @Column(name = "total_amount")
    private double totalAmount;
    @Column(name = "overdue_amount")
    private double overdueAmount;
    @Column(name = "status")
    private String status;
    @Column(name = "code")
    private String code;
    @Column(name = "note")
    private String note;
    @Column(name = "take_date")
    private LocalDate takeDate;
    @Column(name = "return_date")
    private LocalDate returnDate;


    @Column(name = "CREATE_BY", nullable = false)
    private String createBy;

    @Column(name = "UPDATE_DATE", nullable = false)
    private LocalDate updateDate;

    @Column(name = "UPDATE_BY", nullable = false)
    private String updateBy;

    @Column(name = "IS_DELETE")
    private Boolean isDelete;

    public String getStatusString() {
        switch (status) {
            case "1":
                return "On hold";
            case "2":
                return "Returned";
            case "3":
                return "Overdue";

            default:
                return "Unknown";
        }
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private User member;

    @ManyToMany
    @JoinTable(name = "ticket_book", joinColumns = @JoinColumn(name = "ticket_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> books;

}
