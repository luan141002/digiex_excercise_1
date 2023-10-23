package com.example.lab_1.services;

import com.example.lab_1.controller.CustomException;
import com.example.lab_1.model.ClassEntity;
import com.example.lab_1.model.Student;
import com.example.lab_1.model.Subject;
import com.example.lab_1.repository.StudentRepo;
import com.example.lab_1.repository.SubjectRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Service
public class StudentServiceImp implements StudentService{
    @Autowired
    private  SubjectRepo subjectRepo;
    @Autowired
    private StudentRepo studentRepo;


    @Override
    public Student addStudent(Student newStudent) {
        int subCount =  subjectRepo.getAllSubByStuID(newStudent.getStuID()).size();
        Boolean isUnique = areElementsUnique(subjectRepo.getAllSubByStuID(newStudent.getStuID()));
        // check whether student email is unique or not
        List<Student> foundStudent = studentRepo.findAll().stream()
                .filter(student ->(
                  student.getStuEmail()
                            .equals(newStudent.getStuEmail().trim())
                          )
                ).toList();
        // check all above conditions
        if(subCount<3 && subCount>5 ){
            throw new CustomException("this student exceeds subject limit");
        } else if (foundStudent.size()>0 || isUnique == false ) {
            throw new CustomException("this student's email already existed");
        }
        return studentRepo.save(newStudent);
    }
    public static boolean areElementsUnique(List<Subject> inputList) {
        Set<String> seenElements = new HashSet<>();

        for (Subject element : inputList) {
            if (seenElements.contains(element.getSubID())) {
                return false; // This element is a duplicate
            }
            seenElements.add(element.getSubID());
        }
        return true; // All elements are unique
    }


    @Override
    public Student updateStudent(Student updatedStudent) {
        return null;
    }

    @Override
    public void deleteStudent(String id) {
        subjectRepo.deleteAllByStuID(id);
        studentRepo.deleteById(id);
    }

    @Override
    public Double getStudentDetail(String id) {
        return subjectRepo.calStuScore(id);
    }

    public Page<Student> findByClassAndSearchKeyWord(String id,String searchKey,Pageable sortedBy){
        Page<Student> lstStudent = studentRepo.findStudentsByName(id,searchKey,sortedBy);
            return lstStudent;
    }
}
