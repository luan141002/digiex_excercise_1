package com.example.lab_1.services;

import com.example.lab_1.model.Subject;

public interface SubjectService {
    public Subject addSubject(Subject newSubject);
    public Subject updateSubject(Subject updateSubject);
    public void deleteSubject(String id);
    public Subject getSubjectDetail(String id);
}
