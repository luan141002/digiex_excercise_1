package com.example.lab_1.model;

import com.example.lab_1.dto.StudentDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;


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
    @Column(name = "stu_id", length = 32)
    private String student_ID;
    private String middleName;
    @Column(name = "first_name", length = 45)
    private String student_First_Name;
    @Column(name = "last_name", length = 45)
    private String student_Last_Name;
    @Column(name = "email", nullable = false, unique = true, length = 45)
    private String student_Email;
    @Column(name = "phone_number", nullable = false, unique = true, length = 12)
    private Integer student_Phone;
    @Column(name = "gender", length = 45)
    private String student_Gender;
    @Column(name = "dob")
    private Date student_Dob;
    @Column(name = "address")
    private String student_Address;

    @Column(name = "class_id")
    private String student_Class_ID;


    public Student(StudentDTO studentDTO) {
        this.student_ID = studentDTO.getStuID();
        this.student_First_Name = studentDTO.getStuFirstName();
        this.student_Last_Name = studentDTO.getStuLastName();
        this.student_Email = studentDTO.getStuEmail();
        this.student_Phone = studentDTO.getStuPhone();
        this.student_Gender = studentDTO.getStuGender();
        this.student_Dob = studentDTO.getStuDob();
        this.student_Address = studentDTO.getStuAddress();
        this.student_Class_ID = studentDTO.getClasID();
    }

    public boolean areAllFieldsNull() {
        return student_ID == null
                && student_First_Name == null
                && student_Last_Name == null
                && student_Email == null
                && student_Phone == null
                && student_Gender == null
                && student_Dob == null
                && student_Address == null
                && student_Class_ID == null;
    }
}
