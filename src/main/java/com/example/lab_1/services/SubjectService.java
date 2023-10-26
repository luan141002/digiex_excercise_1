package com.example.lab_1.services;

import com.example.lab_1.model.Subject;

import java.util.List;

public interface SubjectService {
    Subject addSubject(Subject newSubject);

    Subject updateSubject(Subject updateSubject);

    void deleteSubject(String id);

    Subject getSubjectDetail(String id);

    public boolean is_Valid(Subject subject, List<Subject> subjectsFromDB);
}
