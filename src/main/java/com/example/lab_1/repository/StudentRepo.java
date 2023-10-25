package com.example.lab_1.repository;

import com.example.lab_1.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface StudentRepo extends JpaRepository<Student,String> {
    @Query("UPDATE student s set s.status =:INACTIVE WHERE s.student_Class_ID =:classID")
    void deleteAllByClassID(@Param("classID") String classID);
    @Query("SELECT s from student s where s.student_Class_ID =:classID")
    List<Student> getAllStuByClassID(@Param("classID") String classID);
    @Query("SELECT s from student s where s.student_Class_ID =:classID")
    List<Student> getAllStuByClassIDWithSort(@Param("classID") String classID, Sort sort);

    @Query("SELECT s FROM student s WHERE s.student_Class_ID = :classID " +
            "AND (s.student_First_Name LIKE %:searchKeywWord% OR s.student_Last_Name LIKE %:searchKeywWord% " +
            "OR s.student_Email LIKE %:searchKeywWord% OR CAST(s.student_Phone AS string) LIKE %:searchKeywWord%)" +
             "AND s.student_Gender LIKE %:gender%")
    Page<Student> findStudentsByName(@Param("classID") String classID,
                                     @Param("searchKeywWord") String searchKeywWord,
                                     @Param("gender") String gender,
                                     Pageable sortedBy);

}
