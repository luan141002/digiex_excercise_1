package com.example.lab_1.services;

import com.example.lab_1.common.enums.Status;
import com.example.lab_1.common.exceptions.ApplicationException;
import com.example.lab_1.common.utils.RestAPIStatus;
import com.example.lab_1.common.utils.UniqueID;
import com.example.lab_1.controller.ClassController;
import com.example.lab_1.controller.CustomException;
import com.example.lab_1.dto.ClassDTO;
import com.example.lab_1.dto.StudentDTO;
import com.example.lab_1.dto.SubjectDTO;
import com.example.lab_1.model.ClassEntity;
import com.example.lab_1.model.Subject;
import com.example.lab_1.repository.ClassRepo;


import com.example.lab_1.repository.StudentRepo;
import com.example.lab_1.repository.SubjectRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;


@Service
public class ClassServiceImp implements ClassService {
    @Autowired
    private ClassRepo classRepo;
    @Autowired
    private StudentRepo stuRepo;
    @Autowired
    private SubjectRepo subjectRepo;
    private static final Logger logger = LoggerFactory.getLogger(ClassServiceImp.class);

    @Override
    public ClassEntity addClass(ClassEntity newClass) {
        if (classRepo.findAll().stream().filter(classEntity -> classEntity.getClass_Name().equals(newClass.getClass_Name().trim())).count() > 0) {
            throw new CustomException("this class name already existed");
        }
        if (newClass.getClass_Max_Student() >= 20) {
            throw new CustomException("this student quantity is over 20 student");
        }
        if (newClass.getStatus() == null) {
            newClass.setStatus(Status.ACTIVE);
        }
        ClassEntity classToSaveEntity = new ClassEntity();
        classToSaveEntity.setClass_ID(UniqueID.getUUID());
        classToSaveEntity.setClass_Name(newClass.getClass_Name());
        classToSaveEntity.setClass_Max_Student(newClass.getClass_Max_Student());
        return classRepo.save(classToSaveEntity);
    }

    @Override
    public ClassEntity updateClass(ClassEntity updatedClass) {
        ClassEntity entityToUpdate = classRepo.findById(updatedClass.getClass_ID()).orElse(null);
        if (entityToUpdate != null) {
            entityToUpdate.setClass_Name(updatedClass.getClass_Name());
            entityToUpdate.setClass_Max_Student(updatedClass.getClass_Max_Student());
            entityToUpdate.setStatus(updatedClass.getStatus());
        }
        if (classRepo.findAll().stream()
                .filter(classEntity -> classEntity.getClass_Name()
                        .equals(updatedClass.getClass_Name().trim())).count() > 0) {
            throw new CustomException("this class name already existed");
        }
        if (updatedClass.getClass_Max_Student() >= 20) {
            throw new CustomException("this student quantity is over 20 student");
        } else {
            return classRepo.save(entityToUpdate);
        }
    }

    @Override
    public void deleteClass(String id) {
        stuRepo.findAll().stream().forEach(student -> {
            if (student.getStudent_Class_ID() != null && student.getStudent_Class_ID().equals(id)) {
                if (stuRepo.getAllStuByClassID(id) != null) {
                    subjectRepo.deleteAllByStuID(student.getStudent_ID());
                }
                if (stuRepo.getAllStuByClassID(id) != null) {
                    stuRepo.deleteAllByClassID(id);
                }
            }
        });

        classRepo.deleteById(id);
    }

    @Override
    public Long getClassDetail(String id) {
        return stuRepo.findAll().stream().filter(student -> student.getStudent_Class_ID() != null && student.getStudent_Class_ID().equals(id)).count();
    }

    @Override
    public List<ClassDTO> getAllClass() {
        List<ClassDTO> dtoList = classRepo.findAll().stream().map(classEntity -> {
            List<StudentDTO> lstStuDTO = stuRepo.getAllStuByClassID(classEntity.getClass_ID()).stream().map(student -> {
                List<Subject> lstSub = subjectRepo.getAllSubByStuID(student.getStudent_ID());
                StudentDTO studentDTO = new StudentDTO(student, lstSub);
                logger.info(String.valueOf(studentDTO));
                return new StudentDTO(student, lstSub);
            }).toList();
            ClassDTO classDTO = new ClassDTO(classEntity, lstStuDTO);
            logger.info(String.valueOf(classDTO));
            return classDTO;
        }).toList();
        return dtoList;
    }

    @Override
    public List<ClassEntity> getStudentListByCategories(Integer type, String className) {
        List<ClassEntity> listClass = new ArrayList<ClassEntity>();
        switch (type) {
            case (1):
                listClass = classRepo.findAll(Sort.by(Sort.Order.asc("claMaxStudent")));
                break;
            case 2:
                listClass = classRepo.findStudentsByName(className);
                break;
            default:
                listClass = classRepo.findAll(Sort.by(Sort.Order.asc("claName")));
                break;
        }
        return listClass;
    }


}
