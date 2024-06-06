package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.BookCategory;
import com.example.BookMangement.Repository.BookCategoryRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.BookCategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * BookCategoryController
 *
 * @author benvo
 * @version 01-00
 * @since 5/13/2024
 */
@Controller
@RequestMapping("/book-category")
public class BookCategoryController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookCategoryRepository bookCategoryRepository;
    @Autowired
    private BookCategoryService bookCategoryService;


    @GetMapping("/new-book-category")
    public String showNewBookForm(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        model.addAttribute("bookCategory", new BookCategory());
        model.addAttribute("listBookCategories", bookCategoryRepository.findAll());
        return "BookCategory/new-book-category";
    }
    @GetMapping("/")
    public String showListBookCategory(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        model.addAttribute("listBookCategory", bookCategoryRepository.findByIsDeleteFalse());
        return "BookCategory/list-book-category";
    }



    @GetMapping("/edit-book-category/{id}")
    public String showUpdateBookCategoryForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        BookCategory bookCategory = bookCategoryService.getBookCategoryById(id);
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        model.addAttribute("bookCategory", bookCategory);
        model.addAttribute("bookCategoryList", bookCategoryList);
        String nameLogin = (String) session.getAttribute("name");
        model.addAttribute("name", nameLogin);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "BookCategory/edit-book-category";
    }

    @GetMapping("/delete-book-category/{id}")
    public String deleteBookCategory(@PathVariable(value = "id") long id) {
        BookCategory bookCategory = bookCategoryService.getBookCategoryById(id);
        bookCategory.setIsDelete(true);
        bookCategoryRepository.save(bookCategory);
        return "redirect:/book-category/";
    }

    @PostMapping("/save-book-category")
    public String saveBookCategory(Authentication authentication,HttpSession session, @Valid @ModelAttribute("bookCategory") BookCategory bookCategory, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            String name = (String) session.getAttribute("name");
            model.addAttribute("name", name);
            String userRole = authentication.getAuthorities().iterator().next().getAuthority();
            model.addAttribute("userRole", userRole);
            model.addAttribute("errorBookCategory", "Fail new book category !");
            return "BookCategory/new-book-category";
        }
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        bookCategory.setCreateDate(LocalDate.now());
        bookCategory.setCreateBy(name);
        bookCategory.setUpdateDate(LocalDate.now());
        bookCategory.setUpdateBy(name);
        bookCategory.setIsDelete(false);
        bookCategoryRepository.save(bookCategory);
        redirectAttributes.addFlashAttribute("newBookCategorySuccess", "Success add new book category!");
        return "redirect:/book-category/new-book-category";
    }


    @PostMapping("/edit-book-category/edit")
    public String editBookCategory(Authentication authentication,Model model,HttpSession session,@Valid @ModelAttribute("bookCategory") BookCategory bookCategory, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            String name = (String) session.getAttribute("name");
            model.addAttribute("name", name);
            String userRole = authentication.getAuthorities().iterator().next().getAuthority();
            model.addAttribute("userRole", userRole);
            model.addAttribute("editBookCategoryError", "Error edit book category !");

            return "Book/edit-book-category";
        }
        String nameLogin = (String) session.getAttribute("name");
        bookCategory.setUpdateBy(nameLogin);
        bookCategory.setUpdateDate(LocalDate.now());
        bookCategory.setIsDelete(false);
        bookCategoryRepository.save(bookCategory);
        redirectAttributes.addFlashAttribute("editBookCategorySuccess", "Success edit book category !");
        return "redirect:/book-category/edit-book-category/" + bookCategory.getId();
    }




}