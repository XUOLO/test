package com.example.BookMangement.Repository;

import com.example.BookMangement.Entity.Ticket;
import com.example.BookMangement.Entity.TicketDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TicketDetailRepository
 *
 * @author xuanl
 * @version 01-00
 * @since 5/20/2024
 */

@Repository
public interface TicketDetailRepository extends JpaRepository<TicketDetail, Long> {
    List<TicketDetail> findByTicket(Ticket ticket);

    @Query("SELECT SUM(td.quantity) FROM TicketDetail td WHERE td.ticket.member = :memberId")
    int getTotalQuantityByMemberId(@Param("memberId") Long memberId);

    List<TicketDetail> findByTicketId(Long ticketId);

    @Query("SELECT td.book, COUNT(td) as rentCount FROM TicketDetail td GROUP BY td.book ORDER BY rentCount DESC")
    List<Object[]> findTopRentedBooks(Pageable pageable);
}
