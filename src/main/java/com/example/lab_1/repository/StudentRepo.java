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
    @Query("UPDATE student s set s.status =:INACTIVE WHERE s.stuClass =:classID")
    void deleteAllByClassID(@Param("classID") String classID);
    @Query("SELECT s from student s where s.stuClass =:classID")
    List<Student> getAllStuByClassID(@Param("classID") String classID);
    @Query("SELECT s from student s where s.stuClass =:classID")
    List<Student> getAllStuByClassIDWithSort(@Param("classID") String classID, Sort sort);
    @Query("SELECT s FROM student s WHERE s.stuClass = :classID " +
           "AND (s.stuFirstName LIKE %:searchKeywWord% OR s.stuLastName LIKE %:searchKeywWord% " +
            "OR s.stuEmail LIKE %:searchKeywWord% OR CAST(s.stuPhone AS string) LIKE %:searchKeywWord%)")
    Page<Student> findStudentsByName(@Param("classID") String classID, @Param("searchKeywWord") String searchKeywWord, Pageable sortedBy);

}
