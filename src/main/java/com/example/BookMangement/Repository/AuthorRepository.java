package com.example.BookMangement.Repository;

import com.example.BookMangement.Entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * AuthorRepository
 *
 * @author xuanl
 * @version 01-00
 * @since 5/13/2024
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Page<Author> findAllByIsDeleteFalse(Pageable pageable);

    @Query("SELECT a FROM Author a WHERE LOWER(a.name) LIKE '%' || LOWER(:keyword) || '%' AND a.isDelete = false")
    List<Author> findAll(@Param("keyword") String keyword);
    @Query("SELECT a FROM Author a WHERE LOWER(a.name) LIKE '%' || LOWER(:keyword) || '%' AND a.isDelete = false")
    Page<Author> findByNameAndIsDeleteFalse(@Param("keyword") String keyword, Pageable pageable);


    @Query("SELECT b FROM Author b "
            + "WHERE ("
            + "LOWER(b.name) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR CAST(b.createDate AS STRING) LIKE '%' || :keyword || '%' "
            + "OR LOWER(b.createBy) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR CAST(b.updateDate AS STRING) LIKE '%' || :keyword || '%' "
            + "OR LOWER(b.updateBy) LIKE '%' || LOWER(:keyword) || '%' "
            + ") "
            + "AND b.isDelete = false")
    Page<Author> findAllByKeyword(@Param("keyword")String keyword, Pageable pageable);

    Set<Author> findByNameIn(List<String> list);
}
