package com.example.lab_1.model;

import com.example.lab_1.common.enums.Status;
import com.example.lab_1.dto.SubjectDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

//@Entity
//@Table(name = "subject")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "subject")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql="UPDATE subject  SET status = 'INACTIVE' WHERE sub_id=?" )
public class Subject extends BaseEntity {
    @Id
    @Column(name = "sub_id", length = 32)
    private String subject_ID;
    @Column(name = "subject_name", length = 45)
    private String subject_Name;
    @Column(name = "number_of_lessons")
    private Integer number_of_lessons;

    @Column(name = "score")
    private Double score;
    @Column(name = "student_id")
    private String subject_student_ID;

    public Subject(SubjectDTO subjectDTO) {
        this.subject_ID = subjectDTO.getSubID();
        this.subject_Name = subjectDTO.getSubName();
        this.number_of_lessons = subjectDTO.getNumberOfLessons();
        this.score = subjectDTO.getScore();
        this.subject_student_ID = subjectDTO.getSubStu();
    }

}
