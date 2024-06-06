package com.example.BookMangement.Repository;

import com.example.BookMangement.Entity.Author;
import com.example.BookMangement.Entity.SendRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * SendRequestRepository
 *
 * @author benvo
 * @version 01-00
 * @since 5/31/2024
 */
public interface SendRequestRepository extends JpaRepository<SendRequest, Long> {
    Page<Author> findAllByIsDeleteFalse(Pageable pageable);

    @Query("SELECT a FROM Author a WHERE LOWER(a.name) LIKE '%' || LOWER(:keyword) || '%' AND a.isDelete = false")
    List<Author> findAll(@Param("keyword") String keyword);
    @Query("SELECT a FROM Author a WHERE LOWER(a.name) LIKE '%' || LOWER(:keyword) || '%' AND a.isDelete = false")
    Page<Author> findByNameAndIsDeleteFalse(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b FROM SendRequest b "
            + "WHERE ("
            + "LOWER(b.description) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR CAST(b.createDate AS STRING) LIKE '%' || :keyword || '%' "
            + "OR LOWER(b.createBy) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR CAST(b.updateDate AS STRING) LIKE '%' || :keyword || '%' "
            + "OR LOWER(b.updateBy) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR LOWER(b.status) LIKE '%' || LOWER(:keyword) || '%' "
            +"OR CAST(b.bookId AS STRING) LIKE '%' || LOWER(:keyword) || '%' "
            + ") "
            + "AND b.isDelete = false")
    Page<SendRequest> findAllByKeyword(String keyword, Pageable pageable);

    @Query("SELECT b FROM SendRequest b "
            + "WHERE ("
            + "LOWER(b.description) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR CAST(b.createDate AS STRING) LIKE '%' || :keyword || '%' "
            + "OR LOWER(b.createBy) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR CAST(b.updateDate AS STRING) LIKE '%' || :keyword || '%' "
            + "OR LOWER(b.updateBy) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR LOWER(b.status) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR CAST(b.bookId AS STRING) LIKE '%' || LOWER(:keyword) || '%' "
            + ") "
            + "AND b.isDelete = false "
            + "AND b.userId = :userId")
    Page<SendRequest> findSendRequestByUserIdAndKeyword(Long userId, String keyword, Pageable pageable);

    @Query("SELECT a.id FROM SendRequest a WHERE a.bookId = :bookId")
    Long getIdByBookId(@Param("bookId") Long bookId);

    @Query("SELECT sr FROM SendRequest sr WHERE sr.bookId = :bookId AND sr.status = :status")
    Optional<SendRequest> findByBookIdAndStatus(@Param("bookId") Long bookId, @Param("status") String status);
}
