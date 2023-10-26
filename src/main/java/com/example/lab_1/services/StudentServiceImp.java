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

import java.util.ArrayList;
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

    public boolean areElementsUnique(List<Subject> inputList) {
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
    public StudentDTO addStudent(StudentDTO newStudent) {
        if (!is_Student_Valid(new Student(newStudent))) {
            throw new CustomException("Cant save this student");
        }
        newStudent.setStuID(UniqueID.getUUID());
        List<Subject> liSubjectToSave = new ArrayList<>();
        if (newStudent.getLstSub().size() > 0) {
            if (newStudent.getLstSub().size() < 3 || newStudent.getLstSub().size() > 5) {
                throw new CustomException("This List is exceed the limit range");
            }
            for (SubjectDTO subject : newStudent.getLstSub()) {
                if (subject.getScore() < 0 || subject.getScore() > 10) {
                    throw new CustomException("subject's score exceed the limit for subject in this student's subject list");
                }
                subject.setSubStu(newStudent.getStuID());
                subject.setSubID(UniqueID.getUUID());
                liSubjectToSave.add(new Subject(subject));
            }
            boolean isUnique = areElementsUnique(liSubjectToSave);
            if (!isUnique) {
                throw new CustomException("There is some subject has the same name");
            }
            subjectRepo.saveAll(liSubjectToSave);
        }


        return new StudentDTO(studentRepo.save(new Student(newStudent)), liSubjectToSave);
    }

    private boolean is_Student_Valid(Student newStudent) {
        // check whether student email is unique or not
        List<Student> foundStudent = studentRepo.findAll().stream()
                .filter(student -> (
                                student.getStudent_Email()
                                        .equals(newStudent.getStudent_Email().trim())
                        )
                ).toList();
        // check all above conditions
        if (foundStudent.size() > 0) {
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
            if (sizeLimit < 3 || sizeLimit > 5) {
                throw new CustomException("Your Subject list exceed the limit");
            }

            logger.info(List_Update_Subject.toString());
            logger.info(List_Create_Subject.toString());
            logger.info(List_Delete_Subject.toString());
            if (!List_Update_Subject.isEmpty()) {
                List<Subject> listUpdatedSubject = listSubject.stream().map(subject -> {
                            Subject testSubject = SavingSubjectChange(subject,
                                    List_Update_Subject.stream().map(Subject::new).toList());
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
            subjectRepo.deleteAll(List_Delete_Subject.stream().map(Subject::new).toList());

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
        if (studentInfo.getClassId() != null) {
            foundStudent.setClassId(studentInfo.getClassId());
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
            subject.setStudentId(foundSubject.getStudentId());
        }
        if (subject.getScore() < 0 || subject.getScore() > 10) {
            throw new CustomException("subject's score exceed the limit for subject in this student's subject list");
        }
        return foundSubject;
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
        if (!requestModel.getGender().equalsIgnoreCase("female") && !requestModel.getGender().equalsIgnoreCase("male")) {
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


}
