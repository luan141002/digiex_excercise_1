package com.example.lab_1.services;

import com.example.lab_1.common.utils.UniqueID;
import com.example.lab_1.controller.CustomException;
import com.example.lab_1.dto.StudentDTO;
import com.example.lab_1.dto.SubjectDTO;
import com.example.lab_1.model.Student;
import com.example.lab_1.model.Subject;
import com.example.lab_1.model.request.FilterRequestModel;
import com.example.lab_1.repository.StudentRepo;
import com.example.lab_1.repository.SubjectRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class StudentServiceImp implements StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImp.class);
    @Autowired
    private SubjectRepo subjectRepo;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private SubjectService subjectService;

    public static boolean areElementsUnique(List<Subject> inputList) {
        Set<String> seenElements = new HashSet<>();

        for (Subject element : inputList) {
            if (seenElements.contains(element.getSubject_ID())) {
                return false; // This element is a duplicate
            }
            seenElements.add(element.getSubject_ID());
        }
        return true; // All elements are unique
    }

    @Override
    public Student addStudent(Student newStudent) {
        if (is_Student_Valid(newStudent)) {
            throw new CustomException("Cant save this student");
        }
        newStudent.setStudent_ID(UniqueID.getUUID());
        return studentRepo.save(newStudent);
    }

    private boolean is_Student_Valid(Student newStudent) {
        int subCount = subjectRepo.getAllSubByStuID(newStudent.getStudent_ID()).size();
        Boolean isUnique = areElementsUnique(subjectRepo.getAllSubByStuID(newStudent.getStudent_ID()));
        // check whether student email is unique or not
        List<Student> foundStudent = studentRepo.findAll().stream()
                .filter(student -> (
                                student.getStudent_Email()
                                        .equals(newStudent.getStudent_Email().trim())
                        )
                ).toList();
        // check all above conditions
        if (subCount < 3 || subCount > 5) {
            throw new CustomException("this student exceeds subject limit");
        }
        if (foundStudent.size() > 0 || !isUnique) {
            throw new CustomException("this student's email already existed");
        }
        return true;
    }

    @Override
    public StudentDTO updateStudent(StudentDTO updateStudent) {
        try {

            Student Student_info = new Student(updateStudent);
            Student foundStudent = studentRepo.findById(updateStudent.getStuID()).orElse(null);
            savingStudentChange(foundStudent, Student_info);
            if (updateStudent.getLstSub().isEmpty()) {
                return updateStudent;
            }


            List<Subject> listSubject = subjectRepo.getAllSubByStuID(updateStudent.getStuID());

            List<SubjectDTO> List_Create_Subject = updateStudent.getLstSub()
                    .stream().filter(subject -> subject.getSubID() == null).toList();
            List<SubjectDTO> List_Update_Subject = updateStudent.getLstSub()
                    .stream().filter(subject -> subject.getSubID() != null
                            && subject.getSubStatus() == null).toList();
            List<SubjectDTO> List_Delete_Subject = updateStudent.getLstSub()
                    .stream().filter(subject ->
                            subject.getSubID() != null
                                    && subject.getSubStatus() != null).toList();
            int sizeLimit = listSubject.size() + List_Create_Subject.size() - List_Delete_Subject.size();
            if (sizeLimit > 5) {
                throw new CustomException("Your Subject list exceed the limit");
            }

            logger.info(List_Update_Subject.toString());
            logger.info(List_Create_Subject.toString());
            logger.info(List_Delete_Subject.toString());
            if (!List_Update_Subject.isEmpty()) {
                List<Subject> listUpdatedSubject = listSubject.stream().map(subject -> {
                            Subject testSubject = SavingSubjectChange(subject,
                                    List_Update_Subject.stream().map(subjectElement -> new Subject(subjectElement)).toList());
                            if (testSubject == null) {
                                return subject;
                            }
                            return testSubject;
                        }
                ).toList();
                subjectRepo.saveAll(listUpdatedSubject);
            }
            subjectRepo.saveAll(List_Create_Subject.stream().map(subject -> {
                subject.setSubID(UniqueID.getUUID());
                subject.setSubStu(updateStudent.getStuID());
                return new Subject(subject);
            }).toList());
            subjectRepo.deleteAll(List_Delete_Subject.stream().map(subject -> new Subject(subject)).toList());

            return new StudentDTO(
                    Student_info, subjectRepo
                    .getAllSubByStuID(updateStudent.getStuID()));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CustomException(ex.getMessage());
        }

    }

    private void savingStudentChange(Student foundStudent, Student studentInfo) {
        if (studentInfo.getStudent_First_Name() != null) {
            foundStudent.setStudent_First_Name(studentInfo.getStudent_First_Name());
        }
        if (studentInfo.getStudent_First_Name() != null) {
            foundStudent.setStudent_First_Name(studentInfo.getStudent_First_Name());
        }
        if (studentInfo.getStudent_Last_Name() != null) {
            foundStudent.setStudent_Last_Name(studentInfo.getStudent_Last_Name());
        }
        if (studentInfo.getStudent_Address() != null) {
            foundStudent.setStudent_Address(studentInfo.getStudent_Address());
        }
        if (studentInfo.getStudent_Email() != null) {
            foundStudent.setStudent_Email(studentInfo.getStudent_Email());
        }
        if (studentInfo.getStudent_Phone() != null) {
            foundStudent.setStudent_Phone(studentInfo.getStudent_Phone());
        }
        if (studentInfo.getStudent_Gender() != null) {
            foundStudent.setStudent_Gender(studentInfo.getStudent_Gender());
        }
        if (studentInfo.getStudent_Class_ID() != null) {
            foundStudent.setStudent_Class_ID(studentInfo.getStudent_Class_ID());
        }
        studentRepo.save(foundStudent);
    }


    private Subject SavingSubjectChange(Subject subject, List<Subject> List_Update_Subject) {

        System.out.println(subject);
        System.out.println(List_Update_Subject);
        Subject foundSubject = List_Update_Subject.stream().filter(subjectElement
                        -> subjectElement.getSubject_ID().equals(subject.getSubject_ID()))
                .findFirst().orElse(null);
        System.out.println(subject);
        System.out.println(List_Update_Subject);
        if (foundSubject == null) {
            return null;
        }
        subject.setSubject_ID(foundSubject.getSubject_ID());
        if (foundSubject.getSubject_Name() != null) {
            subject.setSubject_Name(foundSubject.getSubject_Name());
        }
        if (foundSubject.getNumber_of_lessons() != null) {
            subject.setNumber_of_lessons(foundSubject.getNumber_of_lessons());
        }
        if (foundSubject.getScore() != null) {
            subject.setSubject_student_ID(foundSubject.getSubject_student_ID());
        }
        if (subject.getScore() < 0 || subject.getScore() > 10) {
            throw new CustomException("subject's score exceed the limit for subject in this student's subject list");
        }
        return foundSubject;
    }

    private void StudentMapper(Student foundStudent, StudentDTO updateStudent) {

        foundStudent.setStudent_First_Name(updateStudent.getStuFirstName());
        foundStudent.setStudent_Last_Name(updateStudent.getStuLastName());
        foundStudent.setStudent_Address(updateStudent.getStuAddress());
        foundStudent.setStudent_Email(updateStudent.getStuEmail());
        foundStudent.setStudent_Dob(updateStudent.getStuDob());
        foundStudent.setStudent_Class_ID(updateStudent.getClasID());
        foundStudent.setStudent_Gender(updateStudent.getStuGender());
        foundStudent.setStudent_Phone(updateStudent.getStuPhone());

    }

    @Override
    public void deleteStudent(String id) {
        subjectRepo.deleteAllByStuID(id);
        studentRepo.deleteById(id);
    }

    @Override
    public Double getStudentDetail(String id) {
        return subjectRepo.calStuScore(id);
    }

    @Override
    public Page<Student> findByClassAndSearchKeyWord(FilterRequestModel requestModel) {
        logger.info(requestModel.toString());
        if (requestModel.getClassID() == null) {
            throw new CustomException("Class ID is invalid");
        }
        if (requestModel.getSearchKey() == null) {
            throw new CustomException("Search term is invalid");
        }
        Pageable sortedBy = PageRequest.of(requestModel.getCurrentPageIndex(), requestModel.getPageSize());
        if (!requestModel.getSortField().trim().equals("student_First_Name")
                && !requestModel.getSortField().trim().equals("student_Last_Name")
                && !requestModel.getSortField().trim().equals("student_Email")
                && !requestModel.getSortField().trim().equals("student_Phone")
        ) {
            throw new CustomException("sort field is invalid");
        }
        if (requestModel.getGender() != "female" && requestModel.getGender() != "male") {
            requestModel.setGender("ale");
        }
        logger.info(requestModel.getSortType());
        if (requestModel.getSortType() == null) {

            throw new CustomException("Sort Type is invalid");
        }
        if (requestModel.getSortType().trim().equals("asc")) {
            sortedBy = PageRequest.of(requestModel.getCurrentPageIndex(), requestModel.getPageSize(), Sort.by(Sort.Order.asc(requestModel.getSortField())));
        }
        if (requestModel.getSortType().trim().equals("desc")) {
            sortedBy = PageRequest.of(requestModel.getCurrentPageIndex(), requestModel.getPageSize(), Sort.by(Sort.Order.desc(requestModel.getSortField())));
        }
//        if (requestModel.getStartDoB() == null) {
//            requestModel.setStartDoB("01/01/1900");
//        }
//        if (requestModel.getStartDoB() == null) {
//            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//            String strEndDate = formatter.format(new Date());
//            requestModel.setEndDoB(strEndDate);
//        }

        Page<Student> lstStudent = studentRepo.findStudentsByName(
                requestModel.getClassID(), requestModel.getSearchKey(),
                requestModel.getGender(), sortedBy);
        return lstStudent;
    }


//    public Page<Student> findByClassAndSearchKeyWord(FilterRequestModel requestModel){
////        Pageable sortedBy = PageRequest.of(0, 3, Sort.by(Sort.Order.asc("stuFirstName")));
////        switch (options){
////            case(1):
////                sortedBy =
////                        PageRequest.of(0, 3, Sort.by(Sort.Order.asc("stuFirstName")));
////                break;
////            case(2):
////                sortedBy =
////                        PageRequest.of(0, 3, Sort.by(Sort.Order.asc("stuLastName")));
////                break;
////            case(3):
////                sortedBy =
////                        PageRequest.of(0, 3, Sort.by(Sort.Order.asc("stuEmail")));
////                break;
////            case(4):
////                sortedBy =
////                        PageRequest.of(0, 3, Sort.by(Sort.Order.asc("stuDob")));
////                break;
////            case(5):
////                sortedBy =
////                        PageRequest.of(0, 3, Sort.by(Sort.Order.asc("stuPhone")));
////                break;
////        };
//        Page<Student> lstStudent = studentRepo.findStudentsByName(id,searchKey,sortedBy);
//        return lstStudent;
//    }


}
