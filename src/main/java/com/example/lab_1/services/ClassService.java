package com.example.lab_1.services;

import com.example.lab_1.common.enums.Status;
import com.example.lab_1.dto.ClassDTO;
import com.example.lab_1.model.ClassEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassService {
    public ClassEntity addClass(String id , String className,Integer max_student,String Status);
    public ClassEntity updateClass(String id, String className, Integer max_student, Status Status);
    public void deleteClass(String id);
    public Long getClassDetail(String id);
    public List<ClassDTO> getAllClass();
    //public Page<ClassEntity> searchAndSort(String searchTerm, int page, int size, String sortBy);

}
