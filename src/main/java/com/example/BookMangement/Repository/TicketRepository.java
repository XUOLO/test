package com.example.BookMangement.Repository;


import com.example.BookMangement.Entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * TicketRepository
 *
 * @author xuanl
 * @version 01-00
 * @since 5/20/2024
 */

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByMemberId(Long memberId);
    @Query("SELECT SUM(t.total) FROM Ticket t WHERE t.member.id = ?1")
    int getTotalByMemberId(Long memberId);
    @Query("SELECT SUM(t.total) FROM Ticket t WHERE t.member.id = ?1 AND t.status = '3'")
    Optional<Integer> getTotalBookByMemberId(Long memberId);
    List<Ticket> findByIsDeleteFalse();

    @Query("SELECT a FROM Ticket a WHERE a.status = '3'")
    List<Ticket> getAllByStatus();
    @Override
    long count();
    @Query("SELECT b FROM Ticket b "
            + "WHERE ("
            + "LOWER(b.code) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR LOWER(b.createBy) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR LOWER(b.note) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR LOWER(b.status) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR LOWER(b.member.name) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR CAST(b.takeDate AS STRING) LIKE '%' || :keyword || '%' "
            + "OR CAST(b.returnDate AS STRING) LIKE '%' || :keyword || '%' "
            + "OR CAST(b.updateDate AS STRING) LIKE '%' || :keyword || '%' "
            + "OR LOWER(b.updateBy) LIKE '%' || LOWER(:keyword) || '%' "
            + ") "
            + "AND b.isDelete = false")
    Page<Ticket> findTicketByKeywordAndIsDeleteFalse(String keyword, Pageable pageable);
}
