package com.example.BookMangement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * TicketItem
 *
 * @author xuanl
 * @version 01-00
 * @since 5/17/2024
 */

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketItem {


    private Long id;

    private String title;

    private int quantity;
    private int availableQuantity;

    private double price;

    private int publishYear;

    private Set<BookCategory> bookCategories;

    private Set<Author> authors;

    private String image;

}
