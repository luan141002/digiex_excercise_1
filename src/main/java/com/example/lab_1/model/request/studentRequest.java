package com.example.lab_1.model.request;

import java.util.Date;

public record studentRequest(

        String stuID,

        String stuFirstName,

        String stuLastName,

        String stuEmail,

        Integer stuPhone,

        String stuGender,

        Date stuDob,

        String stuAddress,

        String stuClass
) {

}
