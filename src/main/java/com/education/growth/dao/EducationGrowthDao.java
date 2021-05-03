package com.education.growth.dao;

import java.util.List;

import com.education.growth.model.*;
import org.springframework.http.ResponseEntity;

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

	List<ElectiveDetail> geteletiveByType(String electiveType);

	ResponseEntity<?> subjectMapping(String payload);

	List<SubjectDetail> getSubjectDetailsBySemester(int semester);

    ResponseEntity<?> getSubjectsListbyStudent(String requset);


	ResponseEntity<?> markEntry(List<MarkEntry> markEntries);
}
