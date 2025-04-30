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

@Configuration
public class SecurityConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    public UserDetailsService userDetailsService(AdminLoginService retailLoginService) {
        return username -> {
            Optional<AdminLogin> retailerLogin = retailLoginService.getUserName(username);
            if (retailerLogin.isEmpty()) {
                throw new UsernameNotFoundException("Reatiler login user Name not found");
            }
            
            return new org.springframework.security.core.userdetails.User(
            		retailerLogin.get().getUserName(),
            		retailerLogin.get().getPassword(),
                    new ArrayList<>()
            );
        };
    }


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

        return http.build();
    }

}
