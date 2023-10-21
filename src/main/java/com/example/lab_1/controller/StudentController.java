package com.example.lab_1.controller;

import com.example.lab_1.dto.StudentDTO;
import com.example.lab_1.dto.SubjectDTO;
import com.example.lab_1.model.Student;
import com.example.lab_1.model.Subject;
import com.example.lab_1.repository.StudentRepo;
import com.example.lab_1.repository.SubjectRepo;
import com.example.lab_1.services.StudentService;
import com.example.lab_1.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("student")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private SubjectRepo subjectRepo;
    @Autowired
    private SubjectService subjectService;

    @GetMapping("/topstudent")
    ResponseEntity<List<StudentDTO>> getListStudentPageByClassID(@RequestParam String performanceCategory){
        Double minScore;
        Double maxScore;

        switch (performanceCategory.trim()) {
            case "excellent":
                minScore = 9.0;
                maxScore = 10.0;
                break;
            case "good":
                minScore = 8.0;
                maxScore = 8.9;
                break;
            case "average":
                minScore = 6.0;
                maxScore = 7.9;
                break;
            case "weak":
                minScore = 0.0;
                maxScore = 5.9;
                break;
            default:
                minScore = 0.0;
                maxScore = 10.0;
                break;
            }
            //map to get the list with avgScore of each student
        List<StudentDTO> dtoStuList = studentRepo.findAll().stream().map(student -> {
               return new StudentDTO(student,subjectRepo.getAllSubByStuID(student.getStuID()));
        }).sorted(Comparator.comparing(StudentDTO::getAvgScore).reversed())
                .collect(Collectors.toList());
        List<StudentDTO> responseStuList = dtoStuList.stream()
                .filter(studentDTO -> studentDTO.getAvgScore()>=minScore
                        &&studentDTO.getAvgScore()<=maxScore).toList();
        return ResponseEntity.status(HttpStatus.OK).body(
                responseStuList
        );

    }

    @PostMapping("/addStudent")
    ResponseEntity<Object> addStudent(@RequestBody StudentDTO newStudent){
        if (newStudent.getLstSub().size()>0){
            for (Subject subject :
                 newStudent.getLstSub()) {
                subjectService.addSubject(subject);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new StudentDTO(studentService.addStudent(new Student(newStudent)),newStudent.getLstSub())
        );
    }
    @PostMapping("/updateStudent")
    ResponseEntity<Object> updateStudent(@RequestBody StudentDTO newStudent){
        if (newStudent.getLstSub().size()>0){
            for (Subject subject :
                    newStudent.getLstSub()) {
                subjectService.updateSubject(subject);
            }
        }else {
            subjectRepo.deleteAllByStuID(newStudent.getStuID());
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new StudentDTO(studentService.addStudent(new Student(newStudent)),newStudent.getLstSub())
        );
    }



}
