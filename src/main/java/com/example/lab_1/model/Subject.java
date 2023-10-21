package com.example.lab_1.model;

import com.example.lab_1.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class Subject extends BaseEntity {
    @Id
    @Column(name="sub_id",length = 32)
    private String subID;
    @Column(name="subject_name",length = 45)
    private String subName;
    @Column(name="number_of_lessons")
    private Integer number_of_lessons;
    @Column(name="score")
    private Double score;
    @Column(name="student_id")
    private String subStu;


}
