package com.example.API;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(
            @RequestBody School school
    ) {
        service.saveSchool(school);
    }

//    @PutMapping("/{id}")
//    public void update(@PathVariable("id") Integer id, @RequestBody School updatedSchool) {
//         School existingSchool = service.findSchoolById(id);
//        if (existingSchool == null) {
//            throw new NotFoundException("School not found with id: " + id);
//        }
//        existingSchool.setName(updatedSchool.getName());
//        existingSchool.setEmail(updatedSchool.getEmail());
//        service.saveSchool(existingSchool);
//    }


    @GetMapping
    public ResponseEntity<List<School>> findAllSchools() {

        return ResponseEntity.ok(service.findAllSchools());
    }

//    @GetMapping("/with-students/{school-id}")
//    public ResponseEntity<FullSchoolRespone> findAllSchools(
//            @PathVariable("school-id") Integer schoolId) {
//        return ResponseEntity.ok(service.findSchoolWithStudent(schoolId));
//    }
}
