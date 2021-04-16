package com.education.growth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.education.growth.service.EducationGrowthImpl;

@RestController
public class EducationGrowthRestController {
	
	@Autowired
	EducationGrowthImpl serviceObj;
	
	@PostMapping
	public ResponseEntity<?> validateUserLogin(@RequestBody String requestPayload){
		return null;
		
		
	}

}
