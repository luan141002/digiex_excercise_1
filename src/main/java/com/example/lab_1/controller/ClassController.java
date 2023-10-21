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


    @GetMapping("/page/{id}")
    ResponseEntity<Page<Student>> getListStudentPageByClassID(@PathVariable String id,@RequestParam(required = false) String searchKey,@RequestParam(required = false) Integer options){
        Pageable sortedBy = PageRequest.of(0, 3, Sort.by(Sort.Order.asc("stuFirstName")));

        switch (options){
            case(1):
                sortedBy =
                        PageRequest.of(0, 3, Sort.by(Sort.Order.asc("stuFirstName")));
                break;
            case(2):
                sortedBy =
                        PageRequest.of(0, 3, Sort.by(Sort.Order.asc("stuLastName")));
                break;
            case(3):
                sortedBy =
                        PageRequest.of(0, 3, Sort.by(Sort.Order.asc("stuEmail")));
                break;
            case(4):
                sortedBy =
                        PageRequest.of(0, 3, Sort.by(Sort.Order.asc("stuDob")));
                break;
            case(5):
                sortedBy =
                        PageRequest.of(0, 3, Sort.by(Sort.Order.asc("stuPhone")));
                break;
        };
        return ResponseEntity.status(HttpStatus.OK).body(
                studentService.findByClassAndSearchKeyWord(id,searchKey,sortedBy)
        );

    }

    @GetMapping("")
    ResponseEntity<List<ClassDTO>> getAllClassList() {
        return ResponseEntity.status(HttpStatus.OK).body(
                classService.getAllClass()
        );
    }
    @GetMapping("/sort/{type}")
    ResponseEntity<List<ClassEntity>> getStudentList(@PathVariable Integer type,@RequestParam(required = false) String className ) {
        List<ClassEntity> listClass = new ArrayList<ClassEntity>();
        switch (type){
            case (1):
               listClass = classRepo.findAll(Sort.by(Sort.Order.asc("claMaxStudent")));
                break;
            case 2 :
                listClass  = classRepo.findStudentsByName(className);
                break;
            default:
                listClass  = classRepo.findAll(Sort.by(Sort.Order.asc("claName")));
                break;
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                listClass
        );
    }
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
    @PostMapping("/adduser")
    ResponseEntity<Object> addClass(@RequestBody ClassEntity newClass) {
        // checking whether that class name already existed
        List<ClassEntity> foundClasses = classRepo.findAll().stream()
                .filter(Class ->Class.getClaName().trim()
                        .equals(newClass.getClaName().trim())).toList();
        // Checking condition for new class (name is unique, max_student is not over 20)
        if (foundClasses.size()>0 || newClass.getClaMaxStudent()>20){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Class Name is already taken");
        }
        if (newClass.getStatus()==null){
            newClass.setStatus(Status.ACTIVE);
        }
        // save and return new class
        return ResponseEntity.status(HttpStatus.OK).body(
               classRepo.save(newClass)
        );
    }
    @PostMapping("/updateuser")
    ResponseEntity<Object> updateClass(@RequestBody ClassEntity updatedClass) {
        // checking whether that class name already existed
        List<ClassEntity> foundClasses = classRepo.findAll().stream()
                .filter(Class ->Class.getClaName().trim()
                        .equals(updatedClass.getClaName().trim())).toList();
        // Checking condition for update class (name is unique, max_student is not over 20)
        if (foundClasses.size()>0 || updatedClass.getClaMaxStudent()>20){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Class Name is already taken");
        }else {
            if (updatedClass.getStatus()==null){
                updatedClass.setStatus(Status.ACTIVE);
            }
            // Update and return updatedClass
            return ResponseEntity.status(HttpStatus.OK).body(
                    classService.updateClass(updatedClass.getClaID(),
                            updatedClass.getClaName(),updatedClass.getClaMaxStudent(),
                            updatedClass.getStatus())
            );
        }
    }

    @DeleteMapping ("delete/{id}")
    ResponseEntity deleteClass(@PathVariable String id) {
        classService.deleteClass(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}
