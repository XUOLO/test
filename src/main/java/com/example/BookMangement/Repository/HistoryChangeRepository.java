package com.example.BookMangement.Repository;

import com.example.BookMangement.Entity.HistoryChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * HistoryChangeRepository
 *
 * @author xuanl
 * @version 01-00
 * @since 6/3/2024
 */
@Repository
public interface HistoryChangeRepository extends JpaRepository<HistoryChange, Long> {
    @Query("SELECT a FROM HistoryChange a WHERE a.requestId = :requestId")
    Long getByRequestId(@Param("requestId") Long requestId);

    List<HistoryChange> findByRequestId(Long id);

}
