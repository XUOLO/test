package com.example.BookMangement.Service.ServiceImpls;

import com.example.BookMangement.Entity.Author;
import com.example.BookMangement.Entity.BaseRes;
import com.example.BookMangement.Entity.BookCategory;
import com.example.BookMangement.Repository.AuthorRepository;
import com.example.BookMangement.Service.AuthorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * AuthorServiceImpl
 *
 * @author xuanl
 * @version 01-00
 * @since 5/13/2024
 */
@Service
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Page<Author> findPaginatedAuthor(int pageNo, int pageSize, String sortField, String sortDirection){
        Sort sort= sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        Pageable pageable= PageRequest.of(pageNo - 1,pageSize,sort);
        return this.authorRepository.findAllByIsDeleteFalse(pageable);
    }

    @Override
    public Page<Author> findPaginatedKeywordAuthor(int pageNo, int pageSize, String sortField, String sortDirection,String keyword){
        Sort sort= sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        Pageable pageable= PageRequest.of(pageNo - 1,pageSize,sort);
        return this.authorRepository.findByNameAndIsDeleteFalse(keyword,pageable);
    }
    @Override
    public Author getAuthorById(Long id) {
        try {
            Optional<Author> user = authorRepository.findById(id);
            return user.orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
        } catch (Exception ex) {
            log.error("AuthorServiceImpl_getAuthorById_Error :", ex);
            return null;
        }

    }

    @Override
    public List<Author> searchAuthor(String keyword) {

        if(keyword!=null){
            return authorRepository.findAll(keyword);
        }
        return authorRepository.findAll();
    }

    @Override
    public BaseRes getCmbSong(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Page<Author> authorPage;

        authorPage = authorRepository.findAllByKeyword(keyword, pageable);

        BaseRes baseRes = new BaseRes();
        baseRes.setStatus("success");
        baseRes.setCode(200);

        if (authorPage.getTotalElements() == 0) {
            baseRes.setMessage("No records found");
        } else {
            baseRes.setMessage("Successful");
        }

        baseRes.setData(authorPage);

        return baseRes;
    }


}
