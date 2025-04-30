package com.allica.allica_bank.config;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.allica.allica_bank.entity.AdminLogin;
import com.allica.allica_bank.service.AdminLoginService;

/**
 * Security configuration class for Spring Security setup.
 * Configures:
 * - HTTP Basic authentication
 * - URL-based access control
 * - UserDetailsService for admin authentication
 * - Password encoder (BCrypt)
 */
@Configuration
public class SecurityConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

	/**
	 * Provides a BCrypt password encoder bean.
	 * Used for encoding and verifying user passwords.
	 * 
	 * @return BCryptPasswordEncoder instance
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Provides a custom UserDetailsService for admin login.
	 * This pulls user details from AdminLoginService based on username.
	 *
	 * @param adminLoginService service to fetch admin login data
	 * @return UserDetailsService bean
	 */
    @Bean
    public UserDetailsService userDetailsService(AdminLoginService retailLoginService) {
        return username -> {
			logger.debug("Attempting to load user by username: {}", username);
            Optional<AdminLogin> retailerLogin = retailLoginService.getUserName(username);
            if (retailerLogin.isEmpty()) {
				logger.warn("Username '{}' not found in AdminLoginService", username);
                throw new UsernameNotFoundException("Reatiler login user Name not found");
            }
			logger.info("User '{}' loaded successfully", username);
            return new org.springframework.security.core.userdetails.User(
            		retailerLogin.get().getUserName(),
            		retailerLogin.get().getPassword(),
                    new ArrayList<>()
            );
        };
    }


	/**
	 * Configures security filter chain for HTTP requests.
	 * - Allows public access to Swagger and customer APIs
	 * - Secures /api/v1/admin/** endpoints with basic authentication
	 * - Disables CSRF and sets frame options for H2 console
	 *
	 * @param http HttpSecurity to configure
	 * @return SecurityFilterChain bean
	 * @throws Exception if configuration fails
	 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring SecurityFilterChain...");
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/customer/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/v1/admin/**").authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults()); // Keeps HTTP Basic for /api/secure/**
		logger.info("SecurityFilterChain setup complete.");
        return http.build();
    }

}
