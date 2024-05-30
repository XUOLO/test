package com.example.BookMangement.Repository;


import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Entity.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * BookRepository
 *
 * @author xuanl
 * @version 01-00
 * @since 5/10/2024
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b "
                  + "WHERE ("
                  + "LOWER(b.title) LIKE '%' || LOWER(:keyword) || '%' "
                  + "OR EXISTS (SELECT 1 FROM b.bookCategories bc WHERE LOWER(bc.title) LIKE '%' || LOWER(:keyword) || '%') "
                  + "OR EXISTS (SELECT 1 FROM b.authors a WHERE LOWER(a.name) LIKE '%' || LOWER(:keyword) || '%') "
                  + "OR CAST(b.price AS STRING) LIKE '%' || LOWER(:keyword) || '%' "
                  + "OR CAST(b.quantity AS STRING) LIKE '%' || LOWER(:keyword) || '%' "
                  + "OR CAST(b.publishYear AS STRING) LIKE '%' || LOWER(:keyword) || '%' "
                  + ") "
                  + "AND b.isDelete = false")
    List<Book> findAll(@Param("keyword") String keyword);

    List<Book> findByIsDeleteFalse();

    @Override
    long count();

    @Query("SELECT b FROM Book b "
            + "WHERE ("
            + "LOWER(b.title) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR EXISTS (SELECT 1 FROM b.bookCategories bc WHERE LOWER(bc.title) LIKE '%' || LOWER(:keyword) || '%') "
            + "OR EXISTS (SELECT 1 FROM b.authors a WHERE LOWER(a.name) LIKE '%' || LOWER(:keyword) || '%') "
            + "OR CAST(b.price AS STRING) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR CAST(b.quantity AS STRING) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR CAST(b.publishYear AS STRING) LIKE '%' || LOWER(:keyword) || '%' "
            + ") "
            + "AND b.isDelete = false")
    Page<Book> findAllByKeyword(@Param("keyword") String keyword,Pageable pageable);

}
