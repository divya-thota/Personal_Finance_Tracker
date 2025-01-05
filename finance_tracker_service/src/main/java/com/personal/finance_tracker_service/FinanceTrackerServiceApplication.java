package com.personal.finance_tracker_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.personal.finance_tracker_service", 
		"com.personal.finance_tracker_service.database",
		"com.personal.finance_tracker_service.repository",
		"com.personal.finance_tracker_service.splitwise"})
@EnableScheduling
@Slf4j
public class FinanceTrackerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceTrackerServiceApplication.class, args);
	}

}
