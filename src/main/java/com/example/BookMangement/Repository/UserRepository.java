package com.example.BookMangement.Repository;


import com.example.BookMangement.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findUsername(String username);


    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
    public User findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
}