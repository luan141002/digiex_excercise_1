package com.example.lab_1.services;

import com.example.lab_1.dto.StudentDTO;
import com.example.lab_1.model.Student;
import com.example.lab_1.model.request.FilterRequestModel;
import org.springframework.data.domain.Page;

public interface StudentService {
    StudentDTO addStudent(StudentDTO newStudent);

    StudentDTO updateStudent(StudentDTO updatedStudent);

    void deleteStudent(String id);

    Double getStudentDetail(String id);

    Page<Student> findByClassAndSearchKeyWord(FilterRequestModel requestModel);

}
