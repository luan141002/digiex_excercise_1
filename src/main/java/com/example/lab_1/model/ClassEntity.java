package com.example.lab_1.model;

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
//@Table(name = "class_table")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "classes")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE classes  SET status = 'INACTIVE' WHERE cla_id = ?")
public class ClassEntity extends BaseEntity {
    @Id

    @Column(name = "cla_id", length = 32)
    private String class_ID;
    @Column(name = "class_name", unique = true, length = 45)
    private String class_Name;
    @Column(name = "max_student")
    private Integer class_Max_Student;


}
