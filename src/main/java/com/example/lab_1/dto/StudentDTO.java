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
    private List<Subject> lstSub;


    public StudentDTO(String stuID, String stuFirstName, String stuLastName, String stuEmail, Integer stuPhone, String stuGender,String clasID, Date stuDob, String stuAddress, List<Subject> lstSub) {
        this.stuID = stuID;
        this.stuFirstName = stuFirstName;
        this.stuLastName = stuLastName;
        this.stuEmail = stuEmail;
        this.stuPhone = stuPhone;
        this.stuGender = stuGender;
        this.stuDob = stuDob;
        this.clasID = clasID;
        this.stuAddress = stuAddress;
        this.lstSub = lstSub;
    }
    public static double calculateAverage(List<Subject> numbers) {
        if (numbers.size()== 0) {
            return 0.0; // Tránh chia cho 0 nếu mảng rỗng.
        }

        double sum = 0.0;
        for (Subject number : numbers) {
            sum += number.getScore();
        }

        return sum / numbers.size();
    }
    public StudentDTO(Student student,List<Subject> subjectList) {
        this.stuID = student.getStuID();
        this.stuFirstName = student.getStuFirstName();
        this.stuLastName = student.getStuLastName();
        this.stuEmail = student.getStuEmail();
        this.stuPhone = student.getStuPhone();
        this.stuGender = student.getStuGender();
        this.clasID = student.getStuClass();
        this.stuDob = student.getStuDob();
        this.stuAddress = student.getStuAddress();
        this.lstSub = subjectList;
        if(subjectList!=null){
            this.avgScore = calculateAverage(subjectList);
        }
        else{
            this.avgScore = 0.0;
        }
    }
    public StudentDTO(Student student) {
        this.stuID = student.getStuID();
        this.stuFirstName = student.getStuFirstName();
        this.stuLastName = student.getStuLastName();
        this.stuEmail = student.getStuEmail();
        this.stuPhone = student.getStuPhone();
        this.stuGender = student.getStuGender();
        this.clasID = student.getStuClass();
        this.stuDob = student.getStuDob();
        this.stuAddress = student.getStuAddress();

    }
    // Getters and setters (or Lombok annotations if you prefer)
}

