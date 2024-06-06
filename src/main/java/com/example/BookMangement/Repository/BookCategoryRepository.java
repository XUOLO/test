package com.example.BookMangement.Repository;

import com.example.BookMangement.Entity.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * BookcategoryReponsitory
 *
 * @author benvo
 * @version 01-00
 * @since 5/13/2024
 */
@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
    Page<BookCategory> findAllByIsDeleteFalse(Pageable pageable);
    @Query("SELECT a FROM BookCategory a WHERE LOWER(a.title) LIKE '%' || LOWER(:keyword) || '%' AND a.isDelete = false")
    List<BookCategory> findAll(@Param("keyword") String keyword);
    @Query("SELECT a FROM BookCategory a WHERE LOWER(a.title) LIKE '%' || LOWER(:keyword) || '%' AND a.isDelete = false")
    Page<BookCategory> findByTitleAndIsDeleteFalse(@Param("keyword") String keyword, Pageable pageable);

    List<BookCategory> findByIsDeleteFalse();

    @Query("SELECT b FROM BookCategory b "
            + "WHERE ("
            + "LOWER(b.title) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR CAST(b.createDate AS STRING) LIKE '%' || :keyword || '%' "
            + "OR LOWER(b.createBy) LIKE '%' || LOWER(:keyword) || '%' "
            + "OR CAST(b.updateDate AS STRING) LIKE '%' || :keyword || '%' "
            + "OR LOWER(b.updateBy) LIKE '%' || LOWER(:keyword) || '%' "
            + ") "
            + "AND b.isDelete = false")
    Page<BookCategory> findAllByKeyword(String keyword, Pageable pageable);

    Set<BookCategory> findByTitleIn(List<String> list);
}
