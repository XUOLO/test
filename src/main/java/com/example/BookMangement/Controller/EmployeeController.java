package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.Role;
import com.example.BookMangement.Entity.User;
import com.example.BookMangement.Repository.RoleRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.UserService;
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
import java.util.HashSet;
import java.util.List;

/**
 * @author xuanl
 * @version 01-00
 * @since 5/08/2024
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;



    // List employee page
    @GetMapping("/")

    public String showListEmployee(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("listUser", userRepository.findEmployeeByIsDeleteFalse());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Employee/list-employee";
    }

    // New employee page
    @GetMapping("/admin/new-employee")
    public String showNewEmployeeForm(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("user", new User());
        model.addAttribute("listRole", roleRepository.findAll());
        return "Employee/new-employee";
    }

    // Show edit employee page by id
    @GetMapping("/admin/edit-employee/{id}")
    public String showUpdateEmployeeForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        User user = userService.getUserById(id);
        List<Role> roleList = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("roleList", roleList);
        String nameLogin = (String) session.getAttribute("name");
        model.addAttribute("name", nameLogin);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Employee/edit-employee";
    }

    // Method edit employee
    @PostMapping("/admin/edit-employee")
    public String editEmployee(Model model,@Valid @ModelAttribute("user") User user,BindingResult bindingResult, @RequestParam("roles") List<Long> roleIds, HttpSession session, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            List<Role> roleList = roleRepository.findAll();
            model.addAttribute("roleList", roleList);
            String nameLogin = (String) session.getAttribute("name");
            model.addAttribute("name", nameLogin);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userRole = authentication.getAuthorities().iterator().next().getAuthority();
            model.addAttribute("userRole", userRole);
            return "Employee/edit-employee";
        }
        String nameLogin = (String) session.getAttribute("name");
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        user.setUpdateBy(nameLogin);
        user.setPassword(user.getPassword());
        user.setUpdateDate(LocalDate.now());
        user.setIsDelete(false);
        user.clearRoles();
        List<Role> roles = roleRepository.findAllById(roleIds);
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("editEmployeeSuccess", "Success edit employee !");
        return "redirect:/employee/admin/edit-employee/" + user.getId();
    }

    // Method delete employee by id
    @GetMapping("/admin/delete-employee/{id}")
    public String deleteEmployee(@PathVariable(value = "id") long id) {
        User user = userService.getUserById(id);
        user.setIsDelete(true);
        userRepository.save(user);
        return "redirect:/employee/";
    }
    //new employee
    @PostMapping("/admin/save-user")
    public String saveUser(HttpSession session, Model model, @Valid @ModelAttribute("user") User user, BindingResult bindingResult, RedirectAttributes redirectAttributes, @RequestParam("roles") List<Long> roleIds) {
        if (bindingResult.hasErrors()) {
            String name = (String) session.getAttribute("name");
            model.addAttribute("name", name);
            model.addAttribute("listRole", roleRepository.findAll());
            return "Employee/new-employee";
        }
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        user.setCreateDate(LocalDate.now());
        user.setCreateBy(name);
        user.setUpdateDate(LocalDate.now());
        user.setUpdateBy(name);
        user.setIsDelete(false);
        List<Role> roles = roleRepository.findAllById(roleIds);
        user.setRoles(new HashSet<>(roles));
        userService.save(user);
        redirectAttributes.addFlashAttribute("newEmployeeSuccess", "Success add new employee !");
        return "redirect:/employee/admin/new-employee";
    }

}
