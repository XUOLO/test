package com.example.BookMangement.Security;


import com.example.BookMangement.Entity.User;
import com.example.BookMangement.Service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
/**
 * fff
 *
 * @author xuanl
 * @version 01-00
 * @since 5/08/2024
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Lấy role của người dùng
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();
        String username = authentication.getName();

        User user = userService.findByUsername(username);

            String redirectUrl  = "/";;
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("user", user );
            request.getSession().setAttribute("name", user.getName());
            request.getSession().setAttribute("userId", user.getId());
            request.getSession().setAttribute("email", user.getEmail());
            request.getSession().setAttribute("phone", user.getPhone());
            request.getSession().setAttribute("address", user.getAddress());
            response.sendRedirect(redirectUrl);
        }

    }
