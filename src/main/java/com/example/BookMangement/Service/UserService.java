package com.example.BookMangement.Service;

import com.example.BookMangement.Entity.User;
import com.example.BookMangement.Repository.RoleRepository;
import com.example.BookMangement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public void save(User user) {
//        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }
    public User findByUsername(String username) {
        return userRepository.findUsername(username);
    }
}
