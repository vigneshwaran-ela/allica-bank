package com.allica.allica_bank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AllicaBankApplication {

    private static final Logger logger = LoggerFactory.getLogger(AllicaBankApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AllicaBankApplication.class, args);
        logger.info("Allica Bank Application started successfully!");
	}
}