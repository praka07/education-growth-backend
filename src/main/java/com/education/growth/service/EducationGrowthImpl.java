package com.education.growth.service;

import java.util.List;

import com.education.growth.model.SubjectDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.education.growth.dao.EducationGrowthDaoImpl;
import com.education.growth.model.BatchDetail;
import com.education.growth.model.ElectiveDetail;
import com.education.growth.model.User;

@Service
public class EducationGrowthImpl implements EducationGrowth {

    @Autowired
    EducationGrowthDaoImpl daoObject;

    @Override
    public ResponseEntity<?> createUser(User requeStaffInformation) {
        return daoObject.createUser(requeStaffInformation);
    }

    @Override
    public ResponseEntity<?> validateUserLogin(String requestPayload) {
        return daoObject.validateUserLogin(requestPayload);
    }

    @Override
    public List<User> getUsersByCreatedBy(int createdBy) {
        return daoObject.getUsersByCreatedBy(createdBy);

    }

    @Override
    public ResponseEntity<?> updateUser(User updateUserInfo) {
        return daoObject.updateUser(updateUserInfo);
    }

    @Override
    public ResponseEntity<?> createBatch(String requestPayload) {
        return daoObject.createBatch(requestPayload);
    }

    @Override
    public List<BatchDetail> getBatches() {
        return daoObject.getBatches();
    }

    @Override
    public List<ElectiveDetail> getElective() {
        return daoObject.getElective();
    }

    @Override
    public ResponseEntity<?> createElectiveSubject(String payload) {

        return daoObject.createElectiveSubject(payload);
    }

    @Override
    public ResponseEntity<?> createNewSubject(SubjectDetail reqSubjectDetail) {
        return daoObject.createNewSubject(reqSubjectDetail);
    }

    @Override
    public List<SubjectDetail> getSubjects() {
        return daoObject.getSubjects();
    }

    @Override
    public ResponseEntity<?> editSubject(SubjectDetail reqSubjectDetail) {
        return daoObject.editSubject(reqSubjectDetail);
    }

    @Override
    public List<ElectiveDetail> geteletiveByType(String electiveType) {
        return daoObject.geteletiveByType(electiveType);
    }

    @Override
    public ResponseEntity<?> subjectMapping(String payload) {
        return daoObject.subjectMapping(payload);
    }

    @Override
    public List<SubjectDetail> getSubjectDetailsBySemester(int semester) {
        return daoObject.getSubjectDetailsBySemester(semester);
    }

}
