package com.education.growth.dao;

import java.util.ArrayList;
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
import com.education.growth.model.User;

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
		String insertBatch = "insert into batch(fromDate,toDate) values ( "
				+ createBatchPayload.getString("startYear") + "," + createBatchPayload.getString("endYear")
				+ ")";
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
		String selectQuery="select batchId, cast(fromDate as varchar) + ' - ' + cast(toDate as varchar) as academicYear from batch";
		try {
			details = jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper(BatchDetail.class));
			return details;
		} catch (Exception e) {
			log.info("getBatches error {}", e);
			return details;
		}
	}

}
