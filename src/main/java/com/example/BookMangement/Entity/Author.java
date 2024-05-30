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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.Set;

/**
 * Author
 *
 * @author xuanl
 * @version 01-00
 * @since 5/13/2024
 */
@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "202403_author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Your name is required")
    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "CREATE_DATE",nullable = false)
    private LocalDate createDate;

    @Column(name = "CREATE_BY",nullable = false)
    private String createBy;

    @Column(name = "UPDATE_DATE",nullable = false)
    private LocalDate updateDate;

    @Column(name = "UPDATE_BY",nullable = false)
    private String updateBy;

    @Column(name = "IS_DELETE",nullable = false)
    private Boolean isDelete;

}
