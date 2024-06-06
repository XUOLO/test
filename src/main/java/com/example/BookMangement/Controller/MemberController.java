package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.BookCategory;
import com.example.BookMangement.Entity.Role;
import com.example.BookMangement.Entity.User;
import com.example.BookMangement.Repository.RoleRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.MemberService;
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
 * MemberController
 *
 * @author benvo
 * @version 01-00
 * @since 5/15/2024
 */
@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MemberService memberService;

    @GetMapping("/")
    public String showListMember(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("listMember", userRepository.findMemberByIsDeleteFalse());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Member/list-member";
    }
    @GetMapping("/new-member")
    public String showNewMemberForm(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("user", new User());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Member/new-member";
    }
    @GetMapping("/edit-member/{id}")
    public String showUpdateMemberForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        User user = userService.getUserById(id);
        List<Role> roleList = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("roleList", roleList);
        String nameLogin = (String) session.getAttribute("name");
        model.addAttribute("name", nameLogin);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Member/edit-member";
    }
    @PostMapping("/edit-member/edit")
    public String editMember(@ModelAttribute("user") User user, @RequestParam("roles") List<Long> roleIds, HttpSession session, RedirectAttributes redirectAttributes) {
        String nameLogin = (String) session.getAttribute("name");

        user.setUpdateBy(nameLogin);
        user.setUsername(user.getUsername());
        user.setPassword(user.getPassword());
        user.setUpdateDate(LocalDate.now());

        user.setIsDelete(false);
        user.clearRoles();
        List<Role> roles = roleRepository.findAllById(roleIds);
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("editMemberSuccess", "Success edit member !");
        return "redirect:/member/edit-member/" + user.getId();
    }
    @GetMapping("/delete-member/{id}")
    public String deleteMember(@PathVariable(value = "id") long id) {
        User user = userService.getUserById(id);
        user.setIsDelete(true);
        userRepository.save(user);
        return "redirect:/member/";
    }
    @PostMapping("/save-member")
    public String save(Authentication authentication,HttpSession session, Model model, @Valid @ModelAttribute("user") User user, BindingResult bindingResult, RedirectAttributes redirectAttributes, @RequestParam("roles") List<Long> roleIds) {
        if (bindingResult.hasErrors()) {
            String name = (String) session.getAttribute("name");
            model.addAttribute("name", name);
            String userRole = authentication.getAuthorities().iterator().next().getAuthority();
            model.addAttribute("userRole", userRole);
            return "Member/new-member";
        }
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        user.setCreateDate(LocalDate.now());
        user.setCreateBy(name);
        user.setUpdateDate(LocalDate.now());
        user.setUpdateBy(name);
        user.setIsDelete(false);
        user.setPassword(user.getPhone());
        user.setUsername(user.getPhone());
        List<Role> roles = roleRepository.findAllById(roleIds);
        user.setRoles(new HashSet<>(roles));
        userService.save(user);
        redirectAttributes.addFlashAttribute("newMemberSuccess", "Success add new member !");
        return "redirect:/member/new-member";
    }

}
