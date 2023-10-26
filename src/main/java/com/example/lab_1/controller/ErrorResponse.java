package com.example.lab_1.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private Long timeStamp;

}
