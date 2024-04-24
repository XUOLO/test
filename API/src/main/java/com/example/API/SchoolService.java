package com.example.API;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;


    public void saveSchool(School schools){
        schoolRepository.save(schools);
    }

    public List<School> findAllSchools(){
        return schoolRepository.findAll();
    }


    public School findSchoolById(Integer id) {
        return schoolRepository.findById(id).orElse(null);
    }
}
