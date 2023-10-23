package com.example.lab_1.services;

import com.example.lab_1.common.enums.Status;
import com.example.lab_1.dto.ClassDTO;
import com.example.lab_1.model.ClassEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassService {
    ClassEntity addClass(ClassEntity newClass);
    ClassEntity updateClass(ClassEntity updatedClass);
    void deleteClass(String id);
    Long getClassDetail(String id);
    List<ClassDTO> getAllClass();
    List<ClassEntity> getStudentListByCategories(Integer type,String className);

}
