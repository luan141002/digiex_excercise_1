package com.example.lab_1.services;

import com.example.lab_1.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {
    public Student addStudent(Student newStudent);
    public Student updateStudent(Student updatedStudent);
    public void deleteStudent(String id);
    public Double getStudentDetail(String id);
    public Page<Student> findByClassAndSearchKeyWord(String id, String searchKey, Pageable sortedBy);
}
