package com.example.lab_1.repository;

import com.example.lab_1.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;


public interface SubjectRepo extends JpaRepository<Subject, String> {
    @Query("UPDATE subject s set s.status =:INACTIVE WHERE s.subStu =:stuID")
    void deleteAllByStuID(@Param("stuID") String stuID);
    @Query("SELECT s from subject s where s.subStu =:stuID")
    List<Subject> getAllSubByStuID(@Param("stuID") String stuID);
    @Query("SELECT AVG(s.score) from subject s where s.subStu =:stuID")
    Double calStuScore(@Param("stuID") String stuID);
}
