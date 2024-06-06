package com.example.BookMangement.Service.ServiceImpls;

import com.example.BookMangement.Entity.BaseRes;
import com.example.BookMangement.Entity.User;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserServiceImpls
 *
 * @author xuanl
 * @version 01-00
 * @since 5/13/2024
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public void save(User user) {

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }
    @Override
    public User findByUsername(String username) {

        return userRepository.findUsername(username);
    }

    @Override
    public User getUserById(Long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            return user.orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        } catch (Exception ex) {
            log.error("UserServiceImpl_getUserById_Error :", ex);
            return null;
        }
    }
    @Override
    public Page<User> findPaginatedEmployee(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.userRepository.findPaginatedEmployee(pageable);
    }

    @Override
    public BaseRes getCmbSong(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Page<User> userPage;

            userPage = userRepository.findEmployeeByKeyword(keyword, pageable);

        BaseRes baseRes = new BaseRes();
        baseRes.setStatus("success");
        baseRes.setCode(200);

        if (userPage.getTotalElements() == 0) {
            baseRes.setMessage("No records found");
        } else {
            baseRes.setMessage("Successful");
        }

        baseRes.setData(userPage);

        return baseRes;
    }

    @Override
    public User viewById(long userId) {
        return userRepository.findById(userId).get();
    }
}
