package com.example.BookMangement.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "202403_USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "email",nullable = false, unique = true)
    private String email;

    @Column(name = "address",nullable = false)
    private String address;

    @Column(name = "phone",nullable = false)
    private String phone;

    @Column(name = "username",nullable = false, unique = true)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "CREATE_DATE",nullable = false)
    private LocalDateTime createDate;

    @Column(name = "CREATE_BY",nullable = false)
    private String createBy;

    @Column(name = "UPDATE_TIME",nullable = false)
    private LocalDateTime updateTime;

    @Column(name = "UPDATE_BY",nullable = false)
    private String updateBy;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "202403_users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;
}