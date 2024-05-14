package com.example.CineMax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CineMaxApplication {

	private static final Logger logger = LoggerFactory.getLogger(CineMaxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CineMaxApplication.class, args);
		logger.info("CineMaxApplication started successfully.");
	}
}

