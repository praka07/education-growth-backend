package com.education.growth.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarkEntry {
	private int autoId;
    private int semester;
    private int subjectId;
    private String subjectCode;
    private String subjectType;
    private float credit;
    private String subjectName;
    private int internalMarks;
    private int externalMarks;
    private int totalMarks;
    private int createdBy;
    private String academicYear;
}
