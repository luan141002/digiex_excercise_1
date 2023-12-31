package com.example.lab_1.model;

import com.example.lab_1.dto.SubjectDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

//@Entity
//@Table(name = "subject")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "subject")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE subject  SET status = 'INACTIVE' WHERE sub_id = ?")
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
    private String studentId;

    public Subject(SubjectDTO subjectDTO) {
        this.subject_ID = subjectDTO.getSubID();
        this.subject_Name = subjectDTO.getSubName();
        this.number_of_lessons = subjectDTO.getNumberOfLessons();
        this.score = subjectDTO.getScore();
        this.studentId = subjectDTO.getSubStu();
    }

}
