package com.example.lab_1.model;

import com.example.lab_1.common.enums.Status;
import com.example.lab_1.dto.StudentDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;



@Table
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "student")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
public class Student extends BaseEntity {
    @Id
    @Column(name="stu_id",length = 32)
    private String stuID;
    @Column(name="first_name",length = 45)
    private String stuFirstName;
    @Column(name="last_name",length = 45)
    private String stuLastName;
    @Column(name="email",nullable = false,unique = true,length = 45)
    private String stuEmail;
    @Column(name="phone_number",nullable = false,unique = true,length = 12)
    private Integer stuPhone;
    @Column(name="gender",length = 45)
    private String stuGender;
    @Column(name="dob")
    private Date stuDob;
    @Column(name="address")
    private String stuAddress;

    @Column(name = "class_id")
    private String stuClass;

    public Student(StudentDTO studentDTO) {
        this.stuID = studentDTO.getStuID();
        this.stuFirstName = studentDTO.getStuFirstName();
        this.stuLastName = studentDTO.getStuLastName();
        this.stuEmail = studentDTO.getStuEmail();
        this.stuPhone = studentDTO.getStuPhone();
        this.stuGender = studentDTO.getStuGender();
        this.stuDob = studentDTO.getStuDob();
        this.stuAddress = studentDTO.getStuAddress();

    }
}
