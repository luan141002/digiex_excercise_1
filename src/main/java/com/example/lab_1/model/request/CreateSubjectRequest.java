package com.example.lab_1.model.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateSubjectRequest {

    private String id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Max(value = 10, message = "Cannot higher than 10")
    @Min(value = 0, message = "Cannot lower than 0")
    private double score;

    @NotNull(message = "Number of Lessons is mandatory")
    private int numberOfLessons;


}
