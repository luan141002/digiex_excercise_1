package com.example.lab_1.model;

import com.example.lab_1.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

//@Entity
//@Table(name = "class_table")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "classes")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
public class ClassEntity extends BaseEntity{
    @Id
    @Column(name="cla_id",length = 32)
    private String claID;
    @Column(name = "class_name",unique = true,length = 45)
    private String claName;
    @Column(name = "max_student")
    private Integer claMaxStudent;


}
