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
        if (!is_Valid(newSubject)) {
            throw new CustomException("Failed to save this subject");
        }
        newSubject.setSubject_ID(UniqueID.getUUID());
        return subjectRepo.save(newSubject);
    }

    @Override
    public Subject updateSubject(Subject updateSubject) {
        if (updateSubject.getSubject_ID() != null && updateSubject.getStatus() == null) {
            Subject foundSubject = subjectRepo.findById(updateSubject.getSubject_ID()).orElse(null);
            if (foundSubject == null) {
                throw new CustomException("this Subject is not found");
            }
            if (is_Valid(updateSubject)) {
                foundSubject.setScore(updateSubject.getScore());
                foundSubject.setSubject_Name(updateSubject.getSubject_Name());
                foundSubject.setNumber_of_lessons(updateSubject.getNumber_of_lessons());
                foundSubject.setSubject_student_ID(updateSubject.getSubject_student_ID());
            }
            return subjectRepo.save(foundSubject);
        } else if (updateSubject.getSubject_ID() == null && updateSubject.getStatus() == null) {
            if (is_Valid(updateSubject)) {
                updateSubject.setSubject_ID(UniqueID.getUUID());
                return subjectRepo.save(updateSubject);
            }
        } else if (updateSubject.getSubject_ID() != null && updateSubject.getStatus() != null) {
            deleteSubject(updateSubject.getSubject_ID());
            return updateSubject;
        }
        return updateSubject;
    }

    @Override
    public boolean is_Valid(Subject subject) {
        List<Subject> foundSubjects = subjectRepo.findAll().stream()
                .filter(subjectElement -> (
                                subject.getSubject_student_ID()
                                        .equals(subject.getSubject_student_ID()) &&
                                        subject.getSubject_ID().equals(subject.getSubject_ID())
                        )
                ).toList();
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
