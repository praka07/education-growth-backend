package com.education.growth.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
	private int autoId;
	private String firstName;
	private String lastName;
	private String contactNumber;
	private int role;
	private boolean active;
	private int createdBy;
	private String emailId;
	private String rollNumber;
	private int batch;
	private String password; 
	private String createdDate;
	
	
	

}
