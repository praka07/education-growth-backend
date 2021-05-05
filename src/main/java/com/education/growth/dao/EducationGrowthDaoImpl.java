package com.education.growth.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.education.growth.model.BatchDetail;
import com.education.growth.model.ElectiveDetail;
import com.education.growth.model.MarkEntry;
import com.education.growth.model.SubjectDetail;
import com.education.growth.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EducationGrowthDaoImpl implements EducationGrowthDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public ResponseEntity<?> createUser(User requestPayLoad) {

		String checkUserPresent = "select count(*) from users where emailId ='" + requestPayLoad.getEmailId() + "'";
		log.info("query to checkStaff exist or not  {} ", checkUserPresent);
		try {
			int status = jdbcTemplate.queryForObject(checkUserPresent, Integer.class);
			if (status > 0) {
				return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
						.body("{ \"message\" : \"emailId already registered, try by new\"}");
			} else {
				SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("createUser");
				SqlParameterSource in = new MapSqlParameterSource().addValue("firstName", requestPayLoad.getFirstName())
						.addValue("lastName", requestPayLoad.getLastName())
						.addValue("emailId", requestPayLoad.getEmailId())
						.addValue("contactNumber", requestPayLoad.getContactNumber())
						.addValue("Batch", requestPayLoad.getBatch()).addValue("role", requestPayLoad.getRole())
						.addValue("createdBy", requestPayLoad.getCreatedBy());

				jdbcCall.execute(in);

				return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"created successfully\"}");

			}
		} catch (Exception e) {
			log.info("error wile create user", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}

	@Override
	public ResponseEntity<?> validateUserLogin(String requestPayload) {
		JSONObject loginPayload = new JSONObject(requestPayload);

		String query = "select * from users where emailId ='" + loginPayload.getString("username") + "' and password='"
				+ loginPayload.getString("password") + "'" + " and active=1";
		log.info("query to validate user {} ", query);

		try {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<User> userResult = jdbcTemplate.query(query, new BeanPropertyRowMapper(User.class));
			if (userResult.isEmpty()) {
				return ResponseEntity.badRequest().body("{ \"message\" : \"invalid user\"}");
			} else {
				return ResponseEntity.ok().body(userResult.get(0));
			}

		} catch (Exception e) {
			log.info("error wile validate login", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<User> getUsersByCreatedBy(int createdBy) {
		String selectQuery = "Select * from users where createdBy =" + createdBy + " and role <> 1";
		log.info("selectQuery -- > {}", selectQuery);
		List<User> users = new ArrayList<User>();
		try {
			users = jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper(User.class));
			return users;
		} catch (Exception e) {
			log.info("error wile getAllUsers login", e);
			return users;

		}
	}

	@Override
	public ResponseEntity<?> updateUser(User updateUserInfo) {
		log.info("Update User request {}", updateUserInfo);
		try {
			int status = jdbcTemplate.update(
					"update users set firstName =?,lastName=?,emailId =?,contactNumber=?,active=? where autoid =?",
					updateUserInfo.getFirstName(), updateUserInfo.getLastName(), updateUserInfo.getEmailId(),
					updateUserInfo.getContactNumber(), updateUserInfo.isActive(), updateUserInfo.getAutoId());
			log.info("updated status {}", status);
			return ResponseEntity.status(HttpStatus.OK).body("{ \"message\" : \"updated successfully\"}");

		} catch (Exception e) {
			log.info("update issue {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}

	@Override
	public ResponseEntity<?> createBatch(String requestPayload) {
		JSONObject createBatchPayload = new JSONObject(requestPayload);
		String insertBatch = "insert into batch(fromDate,toDate) values ( " + createBatchPayload.getString("startYear")
				+ "," + createBatchPayload.getString("endYear") + ")";
		try {
			jdbcTemplate.update(insertBatch);
			return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Batch created  successfully \"}");

		} catch (Exception e) {
			log.info("createBatch error {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<BatchDetail> getBatches() {
		List<BatchDetail> details = new ArrayList<BatchDetail>();
		String selectQuery = "select batchId, cast(fromDate as varchar) + ' - ' + cast(toDate as varchar) as academicYear, (select count(*) from users where batch = a.batchId) as numberOfStudent from batch as a";
		try {
			details = jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper(BatchDetail.class));
			return details;
		} catch (Exception e) {
			log.info("getBatches error {}", e);
			return details;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ElectiveDetail> getElective() {
		List<ElectiveDetail> details = new ArrayList<ElectiveDetail>();
		String selectQuery = "select * from elective";
		try {
			details = jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper(ElectiveDetail.class));
			return details;
		} catch (Exception e) {
			log.info("getElective error {}", e);
			return details;
		}
	}

	@Override
	public ResponseEntity<?> createElectiveSubject(String payload) {
		JSONObject requestPayload = new JSONObject(payload);
		String query = "insert into elective(electiveName,electiveType) values ('"
				+ requestPayload.getString("electiveName") + "','" + requestPayload.getString("electiveType") + "')";
		log.info("insert elective query ::: {}", query);
		try {
			jdbcTemplate.update(query);
			return ResponseEntity.status(HttpStatus.OK)
					.body("{\"message\":\"Elective Subject created  successfully \"}");

		} catch (Exception e) {
			log.info("create elective error {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}

	@Override
	public ResponseEntity<?> createNewSubject(SubjectDetail reqSubjectDetail) {
		String query = "insert into subject(subjectCode,subjectName,subjectType,semester,"
				+ "credit,createdBy,batch) values (:subjectCode,:subjectName,:subjectType,:semester,"
				+ ":credit,:createdBy,:batch) ";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("subjectCode", reqSubjectDetail.getSubjectCode());
		parameters.put("subjectName", reqSubjectDetail.getSubjectName());
		parameters.put("subjectType", reqSubjectDetail.getSubjectType());
		parameters.put("semester", reqSubjectDetail.getSemester());
		parameters.put("credit", reqSubjectDetail.getCredit());
		parameters.put("createdBy", reqSubjectDetail.getCreatedBy());
		parameters.put("batch", reqSubjectDetail.getBatch());
		log.info(" create new subject query ::: {}", query);
		parameters.put("createdBy", reqSubjectDetail.getCreatedBy());

		try {
			namedParameterJdbcTemplate.update(query, parameters);
			return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\" Subject created  successfully \"}");
		} catch (Exception e) {
			log.info("createNewSubject error {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Override
	public List<SubjectDetail> getSubjects() {

		List<SubjectDetail> subjectDetails = new ArrayList<SubjectDetail>();
		String selectQuery = "select *,(select cast(fromDate as varchar) + ' - ' + cast(toDate as varchar) from batch where batchid=sub.batch) as academicYear from subject as sub";
		try {
			subjectDetails = jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper(SubjectDetail.class));
			return subjectDetails;
		} catch (Exception e) {
			log.info("getSubjects error {}", e);
			return subjectDetails;
		}
	}

	@Override
	public ResponseEntity<?> editSubject(SubjectDetail reqSubjectDetail) {

		log.info("Update subject Detail {}", reqSubjectDetail);
		try {
			int status = jdbcTemplate.update("update subject set active =?", reqSubjectDetail.isActive());
			log.info("updated status {}", status);
			return ResponseEntity.status(HttpStatus.OK).body("{ \"message\" : \"updated successfully\"}");

		} catch (Exception e) {
			log.info("update Subject {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}

	@Override
	public List<ElectiveDetail> geteletiveByType(String electiveType) {

		List<ElectiveDetail> details = new ArrayList<ElectiveDetail>();
		String selectQuery = "select * from elective where electiveType='" + electiveType + "'";
		try {
			details = jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper(ElectiveDetail.class));
			return details;
		} catch (Exception e) {
			log.info("geteletiveByType error {}", e);
			return details;
		}
	}

	@Override
	public ResponseEntity<?> subjectMapping(String payload) {

		log.info(" subjectMapping {}", payload);
		JSONObject object = new JSONObject(payload);
		try {
			int status = jdbcTemplate.update(
					"insert into subjectMapping (studentId,semester,subjectId,electiveId,createdBy) values"
							+ "(?,?,?,?,?)",
					object.getInt("studentId"), object.getInt("semester"), object.getString("subjectId"),
					object.getString("electiveId"), object.getInt("createdBy"));
			log.info("updated status {}", status);
			return ResponseEntity.status(HttpStatus.OK).body("{ \"message\" : \"mapped successfully\"}");

		} catch (Exception e) {
			log.info("subjectMapping {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}

	@Override
	public List<SubjectDetail> getSubjectDetailsBySemester(int semester) {
		List<SubjectDetail> subjectDetails = new ArrayList<SubjectDetail>();
		String selectQuery = "select * from subject where semester =" + semester + " and subjectType in (2,3)";
		try {
			subjectDetails = jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper(SubjectDetail.class));
			return subjectDetails;
		} catch (Exception e) {
			log.info("getSubjects error {}", e);
			return subjectDetails;
		}
	}

	@Override
	public ResponseEntity<?> getSubjectsListbyStudent(String requset) {
		JSONObject jsonRequest = new JSONObject(requset);
		log.info(" Request >--> {}", jsonRequest);
		try {

			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("getSubjectsListbyStudent");
			SqlParameterSource in = new MapSqlParameterSource().addValue("autoId", jsonRequest.getInt("autoId"))
					.addValue("semester", jsonRequest.getInt("semester"))
					.addValue("createdBy", jsonRequest.getInt("createdBy"));
			Map<String, Object> out = jdbcCall.execute(in);
			log.info("response from store procedure  :::{}", out);
			return ResponseEntity.ok().body(out);

		} catch (Exception e) {
			log.info("getSubjectsListbyStudent {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> markEntry(List<MarkEntry> markEntries) {
		ObjectMapper mapper = new ObjectMapper();

		log.info("markEntries object ::: {}", markEntries);
		try {
			SQLServerDataTable table = new SQLServerDataTable();
			table.addColumnMetadata("autoId", java.sql.Types.INTEGER);
			table.addColumnMetadata("studentId", java.sql.Types.INTEGER);
			table.addColumnMetadata("semester", java.sql.Types.INTEGER);
			table.addColumnMetadata("subjectId", java.sql.Types.INTEGER);
			table.addColumnMetadata("internalMarks", java.sql.Types.INTEGER);
			table.addColumnMetadata("externalMarks", java.sql.Types.INTEGER);
			table.addColumnMetadata("totalMarks", java.sql.Types.INTEGER);
			table.addColumnMetadata("creditPoints", java.sql.Types.DECIMAL);
			table.addColumnMetadata("createdBy", java.sql.Types.INTEGER);
			for (MarkEntry obj : markEntries) {
				log.info("-- Object--{}", mapper.writeValueAsString(obj));
				table.addRow(obj.getAutoId(), obj.getSubjectId(), obj.getSemester(), obj.getSubjectId(),
						obj.getInternalMarks(), obj.getExternalMarks(), obj.getTotalMarks(), obj.getCredit(),
						obj.getCreatedBy());
			}
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("markDetailsUpdate");
			SqlParameterSource in = new MapSqlParameterSource().addValue("objType", table);
			Map<String, Object> out = jdbcCall.execute(in);
			log.info("whole result :::{}", mapper.writeValueAsString(out));
			return ResponseEntity.status(HttpStatus.OK).body("{ \"message\" : \"success\"}");
		} catch (Exception e) {
			log.info("markEntry {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

	}

}
