package com.education.growth.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubjectDetail {
    private int subjectId;
    private String subjectCode;
    private String subjectName;
    private String subjectType;
    private int semester;
    private float credit;
    private int createdBy;
    private boolean active;

}
