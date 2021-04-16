package com.education.growth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class EducationGrowthBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EducationGrowthBackendApplication.class, args);
		log.info("== backend server is up and running with port == 2020");
	}

}
