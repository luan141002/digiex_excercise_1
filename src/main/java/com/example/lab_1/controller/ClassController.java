package com.example.lab_1.controller;

import com.example.lab_1.dto.ClassDTO;
import com.example.lab_1.dto.StudentDTO;
import com.example.lab_1.model.ClassEntity;
import com.example.lab_1.model.Student;
import com.example.lab_1.model.request.FilterRequestModel;
import com.example.lab_1.repository.StudentRepo;
import com.example.lab_1.repository.SubjectRepo;
import com.example.lab_1.services.ClassService;
import com.example.lab_1.services.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/class")

public class ClassController {
    private static final Logger logger = LoggerFactory.getLogger(ClassController.class);
    @Autowired
    private ClassService classService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private SubjectRepo subjectRepo;

    // Path : /class/page/{id}
    @GetMapping("/page/{id}")
    ResponseEntity<Page<Student>> getListStudentPageByClassIDAndCategories(
            @PathVariable String id
            , @RequestParam() String searchKey
            , @RequestParam() String SortField
            , @RequestParam() String SortType
            , @RequestParam(defaultValue = "3") Integer PageSize
            , @RequestParam(defaultValue = "0") Integer CurrentPage
            , @RequestParam(required = false) String StartDoB
            , @RequestParam(required = false) String EndDoB
            , @RequestParam(required = false) String Gender
    ) {
        FilterRequestModel requestModel = FilterRequestModel.builder().ClassID(id)
                .SearchKey(searchKey).SortField(SortField)
                .SortType(SortType).PageSize(PageSize)
                .CurrentPageIndex(CurrentPage).StartDoB(StartDoB)
                .EndDoB(EndDoB).Gender(Gender).build();

        return ResponseEntity.status(HttpStatus.OK).body(
                studentService.findByClassAndSearchKeyWord(requestModel)
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
    ResponseEntity<Long> getClassDetailByID(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                classService.getClassDetail(id)
        );
    }

    // Path : /sort/{type}
    @GetMapping("/sort/{type}")
    ResponseEntity<List<ClassEntity>> getStudentList(
            @PathVariable Integer type,
            @RequestParam(required = false) String className) {
        return ResponseEntity.status(HttpStatus.OK).body(
                classService.getStudentListByCategories(type, className)
        );
    }

    // Path : /class/{id}
    @GetMapping("/{id}")
    ResponseEntity<List<StudentDTO>> getStudentListByClassID(@PathVariable String id) {
        List<StudentDTO> dtoStuList = studentRepo.getAllStuByClassID(id).stream().map(student -> {
                    StudentDTO studentDTO = new StudentDTO(student);
                    Double avgScore = subjectRepo.calStuScore(student.getStudent_ID());
                    if (avgScore != null) {
                        studentDTO.setAvgScore(avgScore);
                    } else {
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
    @PutMapping("/updateClass")
    ResponseEntity<Object> updateClass(@RequestBody ClassEntity updatedClass) {
        // Update and return updatedClass
        logger.info(updatedClass.toString());
        return ResponseEntity.status(HttpStatus.OK).body(
                classService.updateClass(updatedClass)
        );

    }

    // Path : /class/delete/{id}
    @DeleteMapping("delete/{id}")
    ResponseEntity deleteClass(@PathVariable String id) {
        classService.deleteClass(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}
