package com.allica.allica_bank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point for the Allica Bank Spring Boot application.
 * 
 * This class bootstraps the application using Spring Boot's auto-configuration
 * and component scanning.
 * 
 * @author vigneshwaran
 */
@SpringBootApplication
public class AllicaBankApplication {

    private static final Logger logger = LoggerFactory.getLogger(AllicaBankApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AllicaBankApplication.class, args);
        logger.info("Allica Bank Application started successfully!");
	}
}