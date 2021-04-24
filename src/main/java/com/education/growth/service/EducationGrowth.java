package com.education.growth.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.education.growth.model.BatchDetail;
import com.education.growth.model.User;

public interface EducationGrowth {

	ResponseEntity<?> createUser(User requeStaffInformation);

	ResponseEntity<?> validateUserLogin(String requestPayload);


	List<User> getUsersByCreatedBy(int createdBy);

	ResponseEntity<?> updateUser(User updateUserInfo);

	ResponseEntity<?> createBatch(String requestPayload);

	List<BatchDetail> getBatches();

}
