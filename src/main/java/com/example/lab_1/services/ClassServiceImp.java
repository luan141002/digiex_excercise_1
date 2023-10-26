package com.example.lab_1.services;

import com.example.lab_1.common.enums.Status;
import com.example.lab_1.common.utils.UniqueID;
import com.example.lab_1.controller.CustomException;
import com.example.lab_1.dto.ClassDTO;
import com.example.lab_1.dto.StudentDTO;
import com.example.lab_1.model.ClassEntity;
import com.example.lab_1.model.Student;
import com.example.lab_1.model.Subject;
import com.example.lab_1.repository.ClassRepo;
import com.example.lab_1.repository.StudentRepo;
import com.example.lab_1.repository.SubjectRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ClassServiceImp implements ClassService {
    private static final Logger logger = LoggerFactory.getLogger(ClassServiceImp.class);
    @Autowired
    private ClassRepo classRepo;
    @Autowired
    private StudentRepo stuRepo;
    @Autowired
    private SubjectRepo subjectRepo;

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
        if (entityToUpdate == null) {
            throw new CustomException("can't find this class");
        }
        if (classRepo.getClassEntitiesByClassNameAndStatus(updatedClass.getClass_Name().trim()) != null) {
            throw new CustomException("this class name already existed");
        }
        if (updatedClass.getClass_Max_Student() >= 20) {
            throw new CustomException("this student quantity is over 20 student");
        }
        entityToUpdate.setClass_Name(updatedClass.getClass_Name());
        entityToUpdate.setClass_Max_Student(updatedClass.getClass_Max_Student());
        entityToUpdate.setStatus(updatedClass.getStatus());
        return classRepo.save(entityToUpdate);

    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public void deleteClass(String id) {
        try {
            List<Student> liStudentByClassId = stuRepo.getAllStuByClassID(id);
            subjectRepo.deleteAllByStudentIdIn(liStudentByClassId.stream().map(Student::getStudent_ID).toList());
            stuRepo.deleteAll(liStudentByClassId);
            classRepo.deleteById(id);
        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }

    }

    @Override
    public Long getClassDetail(String id) {
        return stuRepo.findAll().stream().filter(student -> student.getClassId() != null && student.getClassId().equals(id)).count();
    }

    @Override
    public List<ClassDTO> getAllClass() {

        List<ClassEntity> classes = classRepo.findAll();
        List<Student> students = stuRepo.findAllByClassIdIn(classes.stream().map(ClassEntity::getClass_ID).toList());
        List<Subject> subjects = subjectRepo.findAllByStudentIdIn(students.stream().map(Student::getStudent_ID).toList());

        return classes.stream().map(classEntity -> {
            List<Student> liStudentByClassId = students.stream()
                    .filter(student -> student.getClassId().equals(classEntity.getClass_ID())).toList();
            List<StudentDTO> lstStuDTO = liStudentByClassId.stream().map(student -> {
                List<Subject> lstSub = subjects.stream()
                        .filter(subject -> subject.getStudentId()
                                .equals(student.getStudent_ID())).toList();
                StudentDTO studentDTO = new StudentDTO(student, lstSub);
                logger.info(String.valueOf(studentDTO));
                return new StudentDTO(student, lstSub);
            }).toList();
            ClassDTO classDTO = new ClassDTO(classEntity, lstStuDTO);
            logger.info(String.valueOf(classDTO));
            return classDTO;
        }).toList();
    }

    @Override
    public List<ClassEntity> getStudentListByCategories(Integer type, String className) {
        logger.info(className);
        logger.info(String.valueOf(type));
        List<ClassEntity> listClass = classRepo.findAll();
        listClass = switch (type) {
            case (1) ->
                    listClass.stream().sorted(Comparator.comparing(ClassEntity::getClass_Max_Student)).collect(Collectors.toList());
            case 2 -> classRepo.findStudentsByName(className);
            default ->
                    listClass.stream().sorted(Comparator.comparing(ClassEntity::getClass_Name)).collect(Collectors.toList());
        };
        return listClass;
    }


}
