package com.example.lab_1.controller;

import com.example.lab_1.common.enums.Status;
import com.example.lab_1.dto.ClassDTO;
import com.example.lab_1.dto.StudentDTO;
import com.example.lab_1.model.ClassEntity;
import com.example.lab_1.model.Student;
import com.example.lab_1.repository.ClassRepo;
import com.example.lab_1.repository.StudentRepo;
import com.example.lab_1.repository.SubjectRepo;
import com.example.lab_1.services.ClassService;
import com.example.lab_1.services.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/class")

public class ClassController {
    @Autowired
    private ClassService classService;
    @Autowired
    private ClassRepo classRepo;
    @Autowired
    private StudentService studentService;
    private static final Logger logger = LoggerFactory.getLogger(ClassController.class);
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private SubjectRepo subjectRepo;

    // Path : /class/page/{id}
    @GetMapping("/page/{id}")
    ResponseEntity<Page<Student>> getListStudentPageByClassID(@PathVariable String id,@RequestParam(required = false) String searchKey,@RequestParam(required = false) Integer options){
        return ResponseEntity.status(HttpStatus.OK).body(
                studentService.findByClassAndSearchKeyWord(id,searchKey,options)
        );
    }
    // Path : /class/
    @GetMapping("")
    ResponseEntity<List<ClassDTO>> getAllClassList() {
        return ResponseEntity.status(HttpStatus.OK).body(
                classService.getAllClass()
        );
    }
    @GetMapping("/{id}/details")
    ResponseEntity<Long> getClassDetailByID(@PathVariable String id ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                classService.getClassDetail(id)
        );
    }

    // Path : /sort/{type}
    @GetMapping("/sort/{type}")
    ResponseEntity<List<ClassEntity>> getStudentList(@PathVariable Integer type,@RequestParam(required = false) String className ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                classService.getStudentListByCategories(type,className)
        );
    }
    // Path : /class/{id}
    @GetMapping("/{id}")
    ResponseEntity<List<StudentDTO>> getStudentListByClassID(@PathVariable String id ) {
        List<StudentDTO> dtoStuList = studentRepo.getAllStuByClassID(id).stream().map(student -> {
           StudentDTO studentDTO = new StudentDTO(student);
           Double avgScore = subjectRepo.calStuScore(student.getStuID());
           if(avgScore!=null){
               studentDTO.setAvgScore(avgScore);
           }else {
               studentDTO.setAvgScore(0.0);
           }

           return studentDTO;
        }).sorted(Comparator.comparing(StudentDTO::getAvgScore))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(
                dtoStuList
        );
    }
    // Path : /class/addClass
    @PostMapping("/addClass")
    ResponseEntity<Object> addClass(@RequestBody ClassEntity newClass) {
        // save and return new class
        return ResponseEntity.status(HttpStatus.OK).body(
               classService.addClass(newClass)
        );
    }
    // Path : /class/updateClass
    @PostMapping("/updateClass")
    ResponseEntity<Object> updateClass(@RequestBody ClassEntity updatedClass) {

            // Update and return updatedClass
            return ResponseEntity.status(HttpStatus.OK).body(
                    classService.updateClass(updatedClass)
            );

    }
    // Path : /class/delete/{id}
    @DeleteMapping ("delete/{id}")
    ResponseEntity deleteClass(@PathVariable String id) {
        classService.deleteClass(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}
