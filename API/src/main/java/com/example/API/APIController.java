package com.example.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class APIController {
    @Autowired
    private SchoolRepository schoolRepository;


    @RequestMapping("/allSchools")
    @ResponseBody
    public List<School> allschol(){
        return schoolRepository.findAll();
    }
}
