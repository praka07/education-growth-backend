package com.education.growth.dao;

import java.util.List;

import com.education.growth.model.SubjectDetail;
import org.springframework.http.ResponseEntity;

import com.education.growth.model.BatchDetail;
import com.education.growth.model.ElectiveDetail;
import com.education.growth.model.User;

public interface EducationGrowthDao {

	ResponseEntity<?> createUser(User requeStaffInformation);

	ResponseEntity<?> validateUserLogin(String requestPayload);


	List<User> getUsersByCreatedBy(int createdBy);

	ResponseEntity<?> updateUser(User updateUserInfo);

	ResponseEntity<?> createBatch(String requestPayload);

	List<BatchDetail> getBatches();

	List<ElectiveDetail> getElective();

	ResponseEntity<?> createElectiveSubject(String payload);


    ResponseEntity<?> createNewSubject(SubjectDetail reqSubjectDetail);

    List<SubjectDetail> getSubjects();

	ResponseEntity<?> editSubject(SubjectDetail reqSubjectDetail);
}
