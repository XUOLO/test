package com.example.BookMangement.Repository;


import com.example.BookMangement.Entity.BookCategory;
import com.example.BookMangement.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * fff
 *
 * @author xuanl
 * @version 01-00
 * @since 5/08/2024
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findUsername(String username);

    List<User> findByIsDeleteFalse();
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles r WHERE r.name IN ('ADMIN','EMPLOYEE') AND u.isDelete = false")
    List<User> findEmployeeByIsDeleteFalse();
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles r WHERE r.name IN ('MEMBER') AND u.isDelete = false")
    List<User> findMemberByIsDeleteFalse();
    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
    public User findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles r WHERE r.name IN ('ADMIN', 'EMPLOYEE') AND u.isDelete = false")
    Page<User> findPaginatedEmployee(Pageable pageable);
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles r WHERE r.name IN ('MEMBER') AND u.isDelete = false")
    Page<User> findPaginatedMember(Pageable pageable);
    @Query("SELECT b "
            + "FROM User b "
            + "LEFT JOIN FETCH b.roles r "
            + "WHERE ("
            + "   LOWER(b.name) LIKE '%' || LOWER(:keyword) || '%' "
            + "   OR LOWER(b.phone) LIKE '%' || LOWER(:keyword) || '%' "
            + "   OR LOWER(b.email) LIKE '%' || LOWER(:keyword) || '%' "
            + "   OR LOWER(b.address) LIKE '%' || LOWER(:keyword) || '%' "
            + "   OR CAST(b.createDate AS STRING) LIKE '%' || :keyword || '%' "
            + "   OR LOWER(b.createBy) LIKE '%' || LOWER(:keyword) || '%' "
            + "   OR CAST(b.updateDate AS STRING) LIKE '%' || :keyword || '%' "
            + "   OR LOWER(b.updateBy) LIKE '%' || LOWER(:keyword) || '%' "
            + ") "
            + "AND b.isDelete = false "
            + "AND r.name = 'MEMBER'")
    Page<User> findMemberByKeywordAndIsDeleteFalse(@Param("keyword") String keyword, Pageable pageable);


    @Query("SELECT b " +
            "FROM User b " +
            "LEFT JOIN FETCH b.roles r " +
            "WHERE (" +
            "   LOWER(b.name) LIKE %:keyword% " +
            "   OR LOWER(b.phone) LIKE %:keyword% " +
            "   OR LOWER(b.email) LIKE %:keyword% " +
            "   OR LOWER(b.address) LIKE %:keyword% " +
            ") " +
            "AND b.isDelete = false " +
            "AND r.name = 'MEMBER'")
    List<User> findAll(@Param("keyword") String keyword);


    @Query("SELECT b "
            + "FROM User b "
            + "LEFT JOIN FETCH b.roles r "
            + "WHERE ("
            + "   LOWER(b.name) LIKE '%' || LOWER(:keyword) || '%' "
            + "   OR LOWER(b.phone) LIKE '%' || LOWER(:keyword) || '%' "
            + "   OR LOWER(b.email) LIKE '%' || LOWER(:keyword) || '%' "
            + "   OR LOWER(b.address) LIKE '%' || LOWER(:keyword) || '%' "
            + "   OR CAST(b.createDate AS STRING) LIKE '%' || :keyword || '%' "
            + "   OR LOWER(b.createBy) LIKE '%' || LOWER(:keyword) || '%' "
            + "   OR CAST(b.updateDate AS STRING) LIKE '%' || :keyword || '%' "
            + "   OR LOWER(b.updateBy) LIKE '%' || LOWER(:keyword) || '%' "
            + ") "
            + "AND b.isDelete = false "
            + "AND (r.name = 'ADMIN' OR r.name = 'EMPLOYEE')")
    Page<User> findEmployeeByKeyword(String keyword, Pageable pageable);
}