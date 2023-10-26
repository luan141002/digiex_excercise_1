package com.example.lab_1.dto;

import com.example.lab_1.model.Student;
import com.example.lab_1.model.Subject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor

public class StudentDTO {
    private String stuID;
    private String stuFirstName;
    private String stuLastName;
    private String stuEmail;
    private Integer stuPhone;
    private String stuGender;
    private Date stuDob;
    private String clasID;
    private String stuAddress;
    private Double avgScore;
    private List<SubjectDTO> lstSub;


    public StudentDTO(String stuID, String stuFirstName, String stuLastName, String stuEmail, Integer stuPhone, String stuGender, String clasID, Date stuDob, String stuAddress, List<Subject> lstSub) {
        this.stuID = stuID;
        this.stuFirstName = stuFirstName;
        this.stuLastName = stuLastName;
        this.stuEmail = stuEmail;
        this.stuPhone = stuPhone;
        this.stuGender = stuGender;
        this.stuDob = stuDob;
        this.clasID = clasID;
        this.stuAddress = stuAddress;
        this.lstSub = lstSub.stream().map(subject -> new SubjectDTO(subject)).toList();
    }

    public StudentDTO(Student student, List<Subject> subjectList) {
        this.stuID = student.getStudent_ID();
        this.stuFirstName = student.getStudent_First_Name();
        this.stuLastName = student.getStudent_Last_Name();
        this.stuEmail = student.getStudent_Email();
        this.stuPhone = student.getStudent_Phone();
        this.stuGender = student.getStudent_Gender();
        this.clasID = student.getClassId();
        this.stuDob = student.getStudent_Dob();
        this.stuAddress = student.getStudent_Address();
        this.lstSub = subjectList.stream().map(subject -> new SubjectDTO(subject)).toList();

        if (subjectList != null) {
            this.avgScore = calculateAverage(this.lstSub);
        } else {
            this.avgScore = 0.0;
        }
    }

    public StudentDTO(Student student) {
        this.stuID = student.getStudent_ID();
        this.stuFirstName = student.getStudent_First_Name();
        this.stuLastName = student.getStudent_Last_Name();
        this.stuEmail = student.getStudent_Email();
        this.stuPhone = student.getStudent_Phone();
        this.stuGender = student.getStudent_Gender();
        this.clasID = student.getClassId();
        this.stuDob = student.getStudent_Dob();
        this.stuAddress = student.getStudent_Address();

    }

    public static double calculateAverage(List<SubjectDTO> numbers) {
        if (numbers.size() == 0) {
            return 0.0; // Tránh chia cho 0 nếu mảng rỗng.
        }

        double sum = 0.0;
        for (SubjectDTO number : numbers) {
            sum += number.getScore();
        }

        return sum / numbers.size();
    }
    // Getters and setters (or Lombok annotations if you prefer)
}

