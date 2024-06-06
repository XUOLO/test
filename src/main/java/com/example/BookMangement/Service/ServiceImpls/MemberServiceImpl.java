package com.example.BookMangement.Service.ServiceImpls;

import com.example.BookMangement.Entity.BaseRes;
import com.example.BookMangement.Entity.Role;
import com.example.BookMangement.Entity.User;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MemberServiceImpl
 *
 * @author benvo
 * @version 01-00
 * @since 5/15/2024
 */
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void save(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public int countMember() {
        List<User> users = userRepository.findByIsDeleteFalse();
        int count = 0;
        for (User user : users) {
            for (Role role : user.getRoles()) {
                if (role.getName().equals("MEMBER")) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    @Override
    public BaseRes getCmbSong(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Page<User> memberPage;

        memberPage = userRepository.findMemberByKeywordAndIsDeleteFalse(keyword, pageable);


        BaseRes baseRes = new BaseRes();
        baseRes.setStatus("success");
        baseRes.setCode(200);

        if (memberPage.getTotalElements() == 0) {
            baseRes.setMessage("No records found");
        } else {
            baseRes.setMessage("Successful");
        }

        baseRes.setData(memberPage);

        return baseRes;
    }

}
