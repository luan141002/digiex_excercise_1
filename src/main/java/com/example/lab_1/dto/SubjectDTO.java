package com.example.lab_1.dto;

import com.example.lab_1.model.Subject;
import lombok.Data;

@Data
public class SubjectDTO {
    private String subID;
    private String subName;
    private Integer numberOfLessons;
    private Double score;
    private String subStu;

    public SubjectDTO() {
        // Default constructor
    }

    public SubjectDTO(String subID, String subName, Integer numberOfLessons, Double score, String subStu) {
        this.subID = subID;
        this.subName = subName;
        this.numberOfLessons = numberOfLessons;
        this.score = score;
        this.subStu = subStu;
    }
    public SubjectDTO(Subject subject){
        this.subID = subject.getSubID();
        this.subName = subject.getSubName();
        this.numberOfLessons = subject.getNumber_of_lessons();
        this.score = subject.getScore();
        this.subStu = subject.getSubStu();
    }
}
