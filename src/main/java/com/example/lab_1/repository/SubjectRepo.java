package com.example.lab_1.repository;

import com.example.lab_1.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SubjectRepo extends JpaRepository<Subject, String> {
    @Query("UPDATE subject s set s.status =:INACTIVE WHERE s.studentId =:stuID")
    void deleteAllByStuID(@Param("stuID") String stuID);

    void deleteAllByStudentIdIn(List<String> liStudentId);

    @Query("SELECT s from subject s where s.studentId =:stuID")
    List<Subject> getAllSubByStuID(@Param("stuID") String stuID);

    List<Subject> findAllByStudentIdIn(List<String> liStudentId);

    @Query("SELECT AVG(s.score) from subject s where s.studentId =:stuID")
    Double calStuScore(@Param("stuID") String stuID);
}
