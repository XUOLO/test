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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.Set;


/**
 * fff
 *
 * @author xuanl
 * @version 01-00
 * @since 5/08/2024
 */
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "202403_USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Your name is required")
    @Column(name = "name",nullable = false)
    private String name;

    @Email(message = "email is invalid!")
    @Column(name = "email",nullable = false)
    private String email;

    @NotBlank(message = "Your address is required")
    @Column(name = "address",nullable = false)
    private String address;

    @NotBlank(message = "Your phone is required")
    @Pattern(regexp = "^(03|05|07|08|09)\\d{8}$", message = " Phone number must start with 03, 05, 07, 08 or 09 and have 10 number")
    @Column(name = "phone")
    private String phone;

    @NotBlank(message = "Your username is required")
    @Column(name = "username",nullable = false)
    private String username;

    @NotBlank(message = "Your password is required")
    @Column(name = "password",nullable = false)
    private String password;

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
    @JoinTable(name = "202403_users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

    public void clearRoles() {
        this.roles.clear();
    }


}