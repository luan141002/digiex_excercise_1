package com.example.lab_1.dto;

import com.example.lab_1.common.enums.Status;
import com.example.lab_1.model.Subject;
import lombok.Data;

@Data
public class SubjectDTO {
    private String subID;
    private String subName;
    private Integer numberOfLessons;
    private Double score;
    private String subStu;
    private String subStatus;

    public SubjectDTO() {
        // Default constructor
    }

    public SubjectDTO(String subID, String subName, Integer numberOfLessons, Double score, String subStu, Status status) {
        this.subID = subID;
        this.subName = subName;
        this.numberOfLessons = numberOfLessons;
        this.score = score;
        this.subStu = subStu;
        this.subStatus = status.toString();
    }
    public SubjectDTO(Subject subject){
        this.subID = subject.getSubject_ID();
        this.subName = subject.getSubject_Name();
        this.numberOfLessons = subject.getNumber_of_lessons();
        this.score = subject.getScore();
        this.subStu = subject.getSubject_student_ID();
        this.subStatus = subject.getStatus().toString();
    }
}
