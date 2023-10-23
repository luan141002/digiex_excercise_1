package com.example.lab_1.services;

import com.example.lab_1.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {
    Student addStudent(Student newStudent);
    Student updateStudent(Student updatedStudent);
    void deleteStudent(String id);
    Double getStudentDetail(String id);
    Page<Student> findByClassAndSearchKeyWord(String id, String searchKey, Integer sortedBy);

}
