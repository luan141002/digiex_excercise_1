package com.example.lab_1.dto;

import com.example.lab_1.model.ClassEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class ClassDTO {
    private String claID;
    private String claName;
    private Integer claMaxStudent;
    private String claStatus;
    private Integer claNumStu;
    private List<StudentDTO> lstStudent;

    public ClassDTO(String claID, String claName, Integer claMaxStudent, String claStatus, List<StudentDTO> dtoList) {
        this.claID = claID;
        this.claName = claName;
        this.claMaxStudent = claMaxStudent;
        this.claStatus = claStatus;
        this.lstStudent = dtoList;
    }
    public ClassDTO(ClassEntity classEntity, List<StudentDTO> dtoList) {
        this.claID = classEntity.getClass_ID();
        this.claName = classEntity.getClass_Name();
        this.claMaxStudent = classEntity.getClass_Max_Student();
        this.lstStudent = dtoList;
        this.claNumStu = dtoList.size();
    }
    public ClassDTO(ClassEntity classEntity) {
        this.claID = classEntity.getClass_ID();
        this.claName = classEntity.getClass_Name();
        this.claMaxStudent = classEntity.getClass_Max_Student();
    }

    // Getters and setters (or Lombok annotations if you prefer)

    public String getClaID() {
        return claID;
    }

    public void setClaID(String claID) {
        this.claID = claID;
    }

    public String getClaName() {
        return claName;
    }

    public void setClaName(String claName) {
        this.claName = claName;
    }

    public Integer getClaMaxStudent() {
        return claMaxStudent;
    }

    public void setClaMaxStudent(Integer claMaxStudent) {
        this.claMaxStudent = claMaxStudent;
    }

    public String getClaStatus() {
        return claStatus;
    }
}
