package com.example.BookMangement.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.Set;

/**
 * Book
 *
 * @author xuanl
 * @version 01-00
 * @since 5/10/2024
 */
@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "202403_Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Your tile is required")
    @Column(name = "title")
    private String title;


    @Column(name = "quantity")
    @Min(value = 1, message = "Your quantity should be greater than or equal to 1")
    private int quantity;

    @Min(value = 1, message = "Your price should be greater than or equal to 1")
    @Column(name = "price")
    private double price;

    @Column(name = "publish_year")
    @Min(value = 1, message = "Your publish year should be greater than or equal to 1")
    @Max(value = 2024, message = "Your publish year should less than or equal to 2024")
    private int publishYear;

    @NotBlank(message = "Your image is required")
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String image;

    @Column(name = "CREATE_DATE",nullable = false)
    private LocalDate createDate;

    @Column(name = "CREATE_BY",nullable = false)
    private String createBy;

    @Column(name = "UPDATE_DATE",nullable = false)
    private LocalDate updateDate;

    @Column(name = "UPDATE_BY",nullable = false)
    private String updateBy;

    @Column(name = "IS_DELETE")
    private Boolean isDelete;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "202403_book_bookcategory",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "bookcategory_id", referencedColumnName = "id")
    )
    private Set<BookCategory> bookCategories;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "202403_book_author",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id")
    )
    private Set<Author> authors;

    public void clearBookCategories() {
        this.bookCategories.clear();
    }

    public void clearAuthors() {
        this.authors.clear();
    }
    @Column(name = "request")
    private String request;
    public String getRequestString() {
        switch (request) {
            case "1":
                return "Pending";
            case "2":
                return "In progress";
            case "3":
                return "Done";
            case "4":
                return "Approve";
            case "5":
                return "Reject";
            default:
                return "Unknown";
        }
    }
}
