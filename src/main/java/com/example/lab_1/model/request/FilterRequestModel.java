package com.example.lab_1.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequestModel {
    private String ClassID;
    private String SearchKey;

    private String SortField;
    private String SortType;

    private Integer PageSize;
    private Integer CurrentPageIndex;

    private String Gender;

    private String StartDoB;
    private String EndDoB;


}
