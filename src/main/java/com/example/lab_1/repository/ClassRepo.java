package com.example.lab_1.repository;

import com.example.lab_1.model.ClassEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ClassRepo extends JpaRepository<ClassEntity, String> {
    //    Page<ClassEntity> findAll(Example<ClassEntity> example, Pageable pageable);
//    Page<ClassEntity> findAll(Example<ClassEntity> example, Pageable pageable, Sort sort);
    @Query("UPDATE classes c set c.status =:INACTIVE WHERE c.class_ID =:classID")
    void deleteAllClassByID(@Param("classID") String classID);

    @Query("SELECT c FROM classes c WHERE c.class_Name =:Name AND  c.status ='ACTIVE'")
    ClassEntity getClassEntitiesByClassNameAndStatus(String Name);

    List<ClassEntity> findAll(Sort sort);

    @Query("SELECT c FROM classes c WHERE c.class_Name LIKE %:clasName%")
    List<ClassEntity> findStudentsByName(@Param("clasName") String clasName);


}

