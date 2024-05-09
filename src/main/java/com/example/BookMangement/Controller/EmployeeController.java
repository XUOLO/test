package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.Role;
import com.example.BookMangement.Entity.User;
import com.example.BookMangement.Repository.RoleRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EmployeeController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/listEmployee")
    public String showListEmployee (Model model, HttpSession session){
        String name = (String) session.getAttribute("name");
        model.addAttribute("name",name);
        model.addAttribute("listUser",userRepository.findAll());
        return "listEmployee";
    }
    @GetMapping("/newEmployee")
    public String showNewEmployeeForm(Model model) {

        model.addAttribute("user", new User());
        model.addAttribute("listRole",  roleRepository.findAll());
        return "newEmployee";
    }

    @GetMapping("/editEmployee/{id}")
    public String showUpdateEmployeeForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        User user = userService.getUserById(id);
        List<Role> roleList = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("roleList", roleList);


            List<String> roles = new ArrayList<>();
            for(Role role : user.getRoles()){
                roles.add(role.getName());
            }

             model.addAttribute("listRole", roles);

        String nameLogin = (String) session.getAttribute("name");
        model.addAttribute("name",nameLogin);
         return "editEmployee";
    }
}
