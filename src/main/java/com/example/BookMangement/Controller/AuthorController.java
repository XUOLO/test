package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.Author;
import com.example.BookMangement.Repository.AuthorRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.AuthorService;
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
 * AuthorController
 *
 * @author xuanl
 * @version 01-00
 * @since 5/13/2024
 */
@Controller
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping("/new-author")
    public String showAuthorForm(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        model.addAttribute("author", new Author());
        return "Author/new-author";
    }


    @GetMapping("/")
    public String showListAuthor(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("listAuthor", authorRepository.findAll());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Author/list-author";
    }

    @GetMapping("/page-author/{page-no}")
    public String findPaginatedAuthor(Authentication authentication, @PathVariable(value = "page-no") int pageNo, Model model, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir) {
        int pageSize = 5;
        Page<Author> page = authorService.findPaginatedAuthor(pageNo, pageSize, sortField, sortDir);
        List<Author> listAuthor = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listAuthor", listAuthor);
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Author/list-author";

    }


    @GetMapping("/edit-author/{id}")
    public String showUpdateAuthorForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        Author author = authorService.getAuthorById(id);
        List<Author> listAuthor = authorRepository.findAll();
        model.addAttribute("author", author);
        model.addAttribute("listAuthor", listAuthor);
        String nameLogin = (String) session.getAttribute("name");
        model.addAttribute("name", nameLogin);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Author/edit-author";
    }

    @GetMapping("/delete-author/{id}")
    public String deleteAuthor(@PathVariable(value = "id") long id) {
        Author author = authorService.getAuthorById(id);
        author.setIsDelete(true);
        authorRepository.save(author);
        return "redirect:/author/";
    }

    @PostMapping("/save-author")
    public String saveAuthor(Authentication authentication,HttpSession session, @Valid @ModelAttribute("author") Author author, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            String name = (String) session.getAttribute("name");
            model.addAttribute("name", name);
            String userRole = authentication.getAuthorities().iterator().next().getAuthority();
            model.addAttribute("userRole", userRole);
            redirectAttributes.addFlashAttribute("errorAuthor", "Fail add new author !");
            return "Author/new-author";
        }
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        author.setCreateDate(LocalDate.now());
        author.setCreateBy(name);
        author.setUpdateDate(LocalDate.now());
        author.setUpdateBy(name);
        author.setIsDelete(false);
        authorRepository.save(author);
        redirectAttributes.addFlashAttribute("newAuthorSuccess", "Success add new author!");
        return "redirect:/author/new-author";
    }


    @PostMapping("/edit-author/edit")
    public String editAuthor(Authentication authentication,Model model,HttpSession session, @Valid @ModelAttribute("author") Author author, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            String name = (String) session.getAttribute("name");
            model.addAttribute("name", name);
            String userRole = authentication.getAuthorities().iterator().next().getAuthority();
            model.addAttribute("userRole", userRole);
            model.addAttribute("errorAuthor", "Fail edit author !");
            return "Author/edit-author";
        }
        String nameLogin = (String) session.getAttribute("name");
        author.setUpdateBy(nameLogin);
        author.setUpdateDate(LocalDate.now());
        author.setIsDelete(false);
        authorRepository.save(author);
        redirectAttributes.addFlashAttribute("editAuthorSuccess", "Success edit author !");
        return "redirect:/author/edit-author/" + author.getId();
    }

    @PostMapping("/search")
    public String searchAuthor(@RequestParam("keyword") String keyword, HttpSession session, Model model, Authentication authentication) {
        List<Author> authors = authorService.searchAuthor(keyword);
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        if (authors.isEmpty()) {
            String errorMessage = "No matching author found";
            model.addAttribute("errorMessage", errorMessage);
            return findPaginatedAuthor(authentication, 1, model, "name", "asc");
        } else {
            model.addAttribute("listAuthor", authors);
        }

        return findPaginatedKeywordAuthor(authentication, 1, model, "name", "asc", keyword);
    }

    @GetMapping("/page-keyword-author/{page-no}")
    public String findPaginatedKeywordAuthor(Authentication authentication, @PathVariable(value = "page-no") int pageNo, Model model, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, @RequestParam("keyword") String keyword) {
        int pageSize = 5;
        Page<Author> page = authorService.findPaginatedKeywordAuthor(pageNo, pageSize, sortField, sortDir, keyword);
        List<Author> listAuthor = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listAuthor", listAuthor);
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Author/list-keyword-author";

    }

}
