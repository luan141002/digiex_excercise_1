package com.example.lab_1.services;

import com.example.lab_1.dto.ClassDTO;
import com.example.lab_1.model.ClassEntity;
import com.example.lab_1.model.request.CreateClassRequest;

import java.util.List;

public interface ClassService {
    ClassEntity addClass(CreateClassRequest newClass);

    ClassEntity updateClass(ClassEntity updatedClass);

    void deleteClass(String id);

    Long getClassDetail(String id);

    List<ClassDTO> getAllClass();

    List<ClassEntity> getStudentListByCategories(Integer type, String className);

}
