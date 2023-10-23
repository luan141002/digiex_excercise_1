package com.example.lab_1.services;

import com.example.lab_1.common.enums.Status;
import com.example.lab_1.common.exceptions.ApplicationException;
import com.example.lab_1.common.utils.RestAPIStatus;
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
public class ClassServiceImp implements ClassService{
    @Autowired
    private ClassRepo classRepo;
    @Autowired
    private StudentRepo stuRepo;
    @Autowired
    private SubjectRepo subjectRepo;
    private static final Logger logger = LoggerFactory.getLogger(ClassService.class);
    @Override
    public ClassEntity addClass(ClassEntity newClass) {
        if(classRepo.findAll().stream().filter(classEntity->classEntity.getClaName().equals(newClass.getClaName().trim())).count()>0 ){
            throw new CustomException("this class name already existed");
        }
        if ( newClass.getClaMaxStudent() >=20) {
            throw new CustomException("this student quantity is over 20 student");
        }
        if (newClass.getStatus()==null){
            newClass.setStatus(Status.ACTIVE);
        }
        return classRepo.save(newClass);
    }

    @Override
    public ClassEntity updateClass(ClassEntity updatedClass) {
        ClassEntity entityToUpdate = classRepo.findById(updatedClass.getClaID()).orElse(null);
        if (entityToUpdate!=null){
            entityToUpdate.setClaName( updatedClass.getClaName());
            entityToUpdate.setClaMaxStudent( updatedClass.getClaMaxStudent());
            entityToUpdate.setStatus(updatedClass.getStatus());
        }
        if(classRepo.findAll().stream()
                .filter(classEntity->classEntity.getClaName()
                        .equals(updatedClass.getClaName().trim())).count()>0){
            throw new CustomException("this class name already existed");
        }
        if (updatedClass.getClaMaxStudent() >=20) {
            throw new CustomException("this student quantity is over 20 student");
        } else {
            return classRepo.save(entityToUpdate);
        }
    }

    @Override
    public void deleteClass(String id) {
        stuRepo.findAll().stream().forEach(student -> {
            if( student.getStuClass()!=null && student.getStuClass().equals(id)){
                if (stuRepo.getAllStuByClassID(id)!=null ) {
                    subjectRepo.deleteAllByStuID(student.getStuID());
                }
                if (stuRepo.getAllStuByClassID(id)!=null ){
                    stuRepo.deleteAllByClassID(id);
                }
            }
        });

        classRepo.deleteById(id);
    }

    @Override
    public Long getClassDetail(String id) {
        return stuRepo.findAll().stream().filter(student ->student.getStuClass()!=null&&student.getStuClass().equals(id)).count();
    }

    @Override
    public List<ClassDTO> getAllClass() {
        List<ClassDTO> dtoList = classRepo.findAll().stream().map(classEntity -> {
            List<StudentDTO> lstStuDTO = stuRepo.getAllStuByClassID(classEntity.getClaID()).stream().map(student -> {
            List<Subject> lstSub = subjectRepo.getAllSubByStuID(student.getStuID());
            StudentDTO studentDTO =  new StudentDTO(student, lstSub);
            logger.info(String.valueOf(studentDTO));
            return new StudentDTO(student, lstSub);
        }).toList();
            ClassDTO classDTO =   new ClassDTO(classEntity,lstStuDTO);
            logger.info(String.valueOf(classDTO));
        return  classDTO ;
    }).toList();
        return dtoList;
    }

    @Override
    public List<ClassEntity> getStudentListByCategories(Integer type,String className) {
        List<ClassEntity> listClass = new ArrayList<ClassEntity>();
        switch (type){
            case (1):
                listClass = classRepo.findAll(Sort.by(Sort.Order.asc("claMaxStudent")));
                break;
            case 2 :
                listClass  = classRepo.findStudentsByName(className);
                break;
            default:
                listClass  = classRepo.findAll(Sort.by(Sort.Order.asc("claName")));
                break;
        }
        return listClass;
    }


}
