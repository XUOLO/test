package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.Role;
import com.example.BookMangement.Entity.User;
import com.example.BookMangement.Repository.RoleRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Controller
public class AuthenController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/")
    public String indexPage(HttpSession session, Model model){

        String name = (String) session.getAttribute("name");
        model.addAttribute("name",name);

        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Principal principal){
        boolean isAuthenticated = principal != null;
         if(isAuthenticated)
         {
             return "redirect:/";
         }
        return "login";
    }



    @PostMapping("/saveUser")
    public String saveUser(HttpSession session, Model model, @Valid @ModelAttribute("user") User user, BindingResult bindingResult, RedirectAttributes redirectAttributes, @RequestParam("roles") List<Long> roleIds) {
        // Lưu người dùng

        if(bindingResult.hasErrors()){
            return "newEmployee";
        }
        String name = (String) session.getAttribute("name");
        model.addAttribute("name",name);
        user.setCreateDate(LocalDateTime.now());
        user.setCreateBy("name");
        user.setUpdateTime(LocalDateTime.now());
        user.setUpdateBy("name");
        userRepository.save(user);

        // Lưu vai trò của người dùng
        List<Role> roles = roleRepository.findAllById(roleIds);
        user.setRoles(new HashSet<>(roles));
        userService.save(user);
        redirectAttributes.addFlashAttribute("newEmployeeSuccess", "Success add new employee !");
        return "redirect:/newEmployee";
    }

}
