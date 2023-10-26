package com.example.lab_1.controller;

import com.example.lab_1.dto.StudentDTO;
import com.example.lab_1.repository.StudentRepo;
import com.example.lab_1.repository.SubjectRepo;
import com.example.lab_1.services.StudentService;
import com.example.lab_1.services.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;


@RestController
@RequestMapping("student")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private SubjectRepo subjectRepo;
    @Autowired
    private SubjectService subjectService;

    // Path : /student/topstudent
    @GetMapping("/topstudent")
    ResponseEntity<List<StudentDTO>> getListStudentPageByClassID(@RequestParam String performanceCategory) {
        double minScore;
        double maxScore;

        switch (performanceCategory.trim()) {
            case "excellent" -> {
                minScore = 9.0;
                maxScore = 10.0;
            }
            case "good" -> {
                minScore = 8.0;
                maxScore = 8.9;
            }
            case "average" -> {
                minScore = 6.0;
                maxScore = 7.9;
            }
            case "weak" -> {
                minScore = 0.0;
                maxScore = 5.9;
            }
            default -> {
                minScore = 0.0;
                maxScore = 10.0;
            }
        }
        //map to get the list with avgScore of each student
        List<StudentDTO> dtoStuList = studentRepo.findAll().stream().map(student -> {
                    return new StudentDTO(student, subjectRepo.getAllSubByStuID(student.getStudent_ID()));
                }).sorted(Comparator.comparing(StudentDTO::getAvgScore).reversed())
                .toList();
        // filter to get the score in the range for specified level
        List<StudentDTO> responseStuList = dtoStuList.stream()
                .filter(studentDTO -> studentDTO.getAvgScore() >= minScore
                        && studentDTO.getAvgScore() <= maxScore).toList();
        if (responseStuList.size() > 3) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    responseStuList.subList(0, 3)
            );
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    responseStuList);
        }
    }

    // Path : /student/addStudent
    @PostMapping("/addStudent")
    ResponseEntity<Object> addStudent(@RequestBody StudentDTO newStudent) throws Exception {

        return ResponseEntity.status(HttpStatus.OK).body(
                studentService.addStudent(newStudent)
        );
    }

    // Path : /student/updateStudent
    @PostMapping("/updateStudent")
    ResponseEntity<Object> updateStudent(@RequestBody StudentDTO updateStudent) {
        logger.info(updateStudent.toString());
        return ResponseEntity.status(HttpStatus.OK).body(
                studentService.updateStudent(updateStudent)
        );
    }


}
