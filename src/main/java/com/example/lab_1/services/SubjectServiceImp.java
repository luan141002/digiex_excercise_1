package com.example.lab_1.services;

import com.example.lab_1.common.enums.Status;
import com.example.lab_1.controller.CustomException;
import com.example.lab_1.model.Student;
import com.example.lab_1.model.Subject;
import com.example.lab_1.repository.SubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SubjectServiceImp implements SubjectService{
    @Autowired
    private SubjectRepo subjectRepo;
    @Override
    public Subject addSubject(Subject newSubject) {
        // check whether there are this subject list of specified student or not
        List<Subject> foundSubject = subjectRepo.findAll().stream()
                .filter(subject ->(
                        subject.getSubStu()
                                .equals(newSubject.getSubStu())&&
                                subject.getSubID().equals(newSubject.getSubID())
                        )
                ).toList();
        if(newSubject.getScore()<0 || newSubject.getScore()>10){
            throw new CustomException("subject's score exceed the limit for subject in this student's subject list");
        } else if (foundSubject.size()>0) {
            throw new CustomException("some subjects already existed in this student's subject list");
        }
        return subjectRepo.save(newSubject);
    }

    @Override
    public Subject updateSubject(Subject updateSubject) {

        Subject foundSubject = subjectRepo.findById(updateSubject.getSubID()).orElse(null);

        if (updateSubject!=null){
            List<Subject> foundSubjects = subjectRepo.findAll().stream()
                    .filter(subject ->(
                                    subject.getSubStu()
                                            .equals(updateSubject.getSubStu())&&
                                            subject.getSubID().equals(updateSubject.getSubID())
                            )
                    ).toList();
            if(updateSubject.getScore()<0 || updateSubject.getScore()>10){
                throw new CustomException("subject's score exceed the limit for subject in this student's subject list");
            } else if (foundSubjects.size()>0) {
                throw new CustomException("some subjects already existed in this student's subject list");
            }
            else{
                foundSubject.setScore(updateSubject.getScore());
                foundSubject.setSubName(updateSubject.getSubName());
                foundSubject.setNumber_of_lessons(updateSubject.getNumber_of_lessons());
                foundSubject.setSubStu(updateSubject.getSubStu());
            }

            return subjectRepo.save(foundSubject);


        }else{
            List<Subject> foundSubjects = subjectRepo.findAll().stream()
                    .filter(subject ->(
                                    subject.getSubStu()
                                            .equals(updateSubject.getSubStu())&&
                                            subject.getSubID().equals(updateSubject.getSubID())
                            )
                    ).toList();
            if(updateSubject.getScore()<0 || updateSubject.getScore()>10){
                throw new CustomException("subject's score exceed the limit for subject in this student's subject list");
            } else if (foundSubjects.size()>0) {
                throw new CustomException("This subject already existed in this student's subject list");
            }

            return subjectRepo.save(updateSubject);
        }

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
