package com.education.growth.controller;

import java.util.List;

import com.education.growth.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.education.growth.service.EducationGrowthImpl;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EducationGrowthRestController {

    @Autowired
    EducationGrowthImpl serviceObj;

    @PostMapping("/validatelogin")
    public ResponseEntity<?> validateUserLogin(@RequestBody String requestPayload) {
        return serviceObj.validateUserLogin(requestPayload);

    }

    @PostMapping("/createuser")
    public ResponseEntity<?> createuser(@RequestBody User requeStaffInformation) {
        return serviceObj.createUser(requeStaffInformation);
    }


    @GetMapping("/getalluser/{createdBy}")
    public List<User> getUsersByCreatedBy(@PathVariable int createdBy) {
        return serviceObj.getUsersByCreatedBy(createdBy);

    }

    @PutMapping("/updateuserdetail")
    public ResponseEntity<?> updateUser(@RequestBody User updateUserInfo) {
        return serviceObj.updateUser(updateUserInfo);

    }

    @PostMapping("/createbatch")
    public ResponseEntity<?> createBatch(@RequestBody String requestPayload) {
        return serviceObj.createBatch(requestPayload);

    }

    @GetMapping("/getbatch")
    public List<BatchDetail> getBatchList() {
        return serviceObj.getBatches();
    }

    @GetMapping("/getelective")
    public List<ElectiveDetail> getElectiveList() {
        return serviceObj.getElective();
    }

    @PostMapping("/createelectivesubject")
    public ResponseEntity<?> createElectiveSubject(@RequestBody String payload) {
        return serviceObj.createElectiveSubject(payload);
    }

    @PostMapping("/createsubject")
    public ResponseEntity<?> createNewSubject(@RequestBody SubjectDetail reqSubjectDetail) {
        return serviceObj.createNewSubject(reqSubjectDetail);
    }

    @GetMapping("/listsubject")
    public List<SubjectDetail> getSubjects() {
        return serviceObj.getSubjects();
    }

    @PutMapping("/editsubject")
    public ResponseEntity<?> editSubject(@RequestBody SubjectDetail reqSubjectDetail) {
        return serviceObj.editSubject(reqSubjectDetail);
    }

    @GetMapping("/eletive/{electiveType}")
    public List<ElectiveDetail> geteletiveByType(@PathVariable String electiveType) {
        return serviceObj.geteletiveByType(electiveType);
    }

    @PostMapping("/subjectmapping")
    public ResponseEntity<?> subjectMapping(@RequestBody String payload) {
        return serviceObj.subjectMapping(payload);
    }

    @GetMapping("/getsubject/{semester}")
    public List<SubjectDetail> getSubjectDetailsBySemester(@PathVariable int semester) {
        return serviceObj.getSubjectDetailsBySemester(semester);
    }

    @PostMapping("/subjectslistbystudent")
    public ResponseEntity<?> getSubjectsListbyStudent(@RequestBody String requset) {
        return serviceObj.getSubjectsListbyStudent(requset);
    }

    @PostMapping("/markentry")
    public ResponseEntity<?> markEntry(@RequestBody List<MarkEntry> markEntries) {
        return serviceObj.markEntry(markEntries);

    }
}
