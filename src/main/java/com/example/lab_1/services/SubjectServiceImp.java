package com.example.lab_1.services;

import com.example.lab_1.common.enums.Status;
import com.example.lab_1.common.utils.UniqueID;
import com.example.lab_1.controller.CustomException;
import com.example.lab_1.model.Subject;
import com.example.lab_1.repository.SubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImp implements SubjectService {
    @Autowired
    private SubjectRepo subjectRepo;

    @Override
    public Subject addSubject(Subject newSubject) {
        // check whether there are this subject list of specified student or not
        List<Subject> subjectFromDb = subjectRepo.findAll();
        List<Subject> foundLiSubject = subjectFromDb.stream()
                .filter(subject -> subject.getStudentId()
                        .equals(newSubject.getStudentId())).toList();
        if (!is_Valid(newSubject, foundLiSubject)) {
            throw new CustomException("Failed to save this subject");
        }
        newSubject.setSubject_ID(UniqueID.getUUID());
        return subjectRepo.save(newSubject);
    }

    @Override
    public Subject updateSubject(Subject updateSubject) {

        List<Subject> subjectFromDb = subjectRepo.findAll();
        List<Subject> foundLiSubject = subjectFromDb.stream()
                .filter(subject -> subject.getStudentId()
                        .equals(updateSubject.getStudentId())).toList();
        Subject foundSubject = foundLiSubject.stream()
                .filter(subject -> subject.getSubject_ID()
                        .equals(updateSubject.getSubject_ID())).findFirst().orElse(null);
        if (foundSubject == null) {
            throw new CustomException("this Subject is not found");
        }
        if (is_Valid(updateSubject, foundLiSubject)) {
            foundSubject.setSubject_ID(foundSubject.getSubject_ID());
            foundSubject.setScore(updateSubject.getScore());
            foundSubject.setSubject_Name(updateSubject.getSubject_Name());
            foundSubject.setNumber_of_lessons(updateSubject.getNumber_of_lessons());
            foundSubject.setStudentId(updateSubject.getStudentId());
        }
        return subjectRepo.save(foundSubject);
    }

    @Override
    public boolean is_Valid(Subject subject, List<Subject> subjectsFromDb) {
        List<Subject> foundSubjects = subjectsFromDb.stream()
                .filter(subjectElement -> subject.getSubject_Name().equals(subject.getSubject_Name())).toList();
        if (subject.getScore() < 0 || subject.getScore() > 10) {
            throw new CustomException("subject's score exceed the limit for subject in this student's subject list");
        } else if (foundSubjects.size() > 0) {
            throw new CustomException("This subject already existed in this student's subject list");
        }
        return true;
    }

    @Override
    public void deleteSubject(String id) {
        Subject foundSubject = subjectRepo.findById(id).orElse(null);
        foundSubject.setStatus(Status.INACTIVE);
    }

    @Override
    public Subject getSubjectDetail(String id) {
        Subject foundSubject = subjectRepo.findById(id).orElse(null);
        return foundSubject;
    }
}
