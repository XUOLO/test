package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.User;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class AuthenController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String indexPage(HttpSession session, Model model){

        String name = (String) session.getAttribute("name");
        model.addAttribute("name",name);
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String submitRegister(HttpSession session,HttpServletRequest request, @ModelAttribute("user") @Valid User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        String name = (String) session.getAttribute("name");
        user.setCreateDate(LocalDateTime.now());
        user.setCreateBy(name);
        user.setUpdateTime(LocalDateTime.now());
        user.setUpdateBy(name);
        userRepository.save(user);

        return "register";
    }
}
