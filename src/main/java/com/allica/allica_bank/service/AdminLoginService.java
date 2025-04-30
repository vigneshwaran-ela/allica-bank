package com.allica.allica_bank.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allica.allica_bank.entity.AdminLogin;
import com.allica.allica_bank.repository.AdminLoginRepository;

/**
 * Service class responsible for handling admin login operations.
 */
@Service
public class AdminLoginService {

	private static final Logger logger = LoggerFactory.getLogger(AdminLoginService.class);

	@Autowired
	private AdminLoginRepository retailerLoginRepo;

	/**
	 * Retrieves a admin login by username.
	 *
	 * @param userName the username to search for
	 * @return an Optional containing the AdminLogin entity if found
	 */
	public Optional<AdminLogin> getUserName(String userName) {
		logger.info("Fetching AdminLogin for username: {}", userName);
		Optional<AdminLogin> login = this.retailerLoginRepo.findByUserName(userName);

		if (login.isEmpty()) {
			logger.warn("No AdminLogin found for username: {}", userName);
		} else {
            logger.debug("AdminLogin found for username: {}", userName);
		}
		return login;
	}
}
