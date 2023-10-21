package com.example.lab_1.repository;

import com.example.lab_1.model.ClassEntity;
import com.example.lab_1.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ClassRepo extends JpaRepository<ClassEntity, String>{
//    Page<ClassEntity> findAll(Example<ClassEntity> example, Pageable pageable);
//    Page<ClassEntity> findAll(Example<ClassEntity> example, Pageable pageable, Sort sort);
    @Query("UPDATE classes c set c.status =:INACTIVE WHERE c.claID =:classID")
    void deleteAllClassByID(@Param("classID") String classID);


    List<ClassEntity> findAll(Sort sort);
    @Query("SELECT c FROM classes c WHERE c.claName LIKE %:clasName%")
    List<ClassEntity> findStudentsByName( @Param("clasName") String clasName);



}

