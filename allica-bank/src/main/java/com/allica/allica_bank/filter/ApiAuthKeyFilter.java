package com.allica.allica_bank.filter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.allica.allica_bank.entity.Retailer;
import com.allica.allica_bank.enums.RetailerType;
import com.allica.allica_bank.repository.RetailerRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiAuthKeyFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiAuthKeyFilter.class);
	private static final String API_KEY_HEADER = "X-API-KEY";
	private static final String RETAILER_HEADER = "X-RETAILER";

	@Autowired
	private RetailerRepository retailerRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String traceId = UUID.randomUUID().toString();
			MDC.put("traceId", traceId);
			logger.info("Trace Id is generated for the request..");
	        String path = request.getRequestURI();
	        if (!path.startsWith("/api/v1/customer")) {
	            filterChain.doFilter(request, response); // skip API key check
	            return;
	        }

			String apiKey = request.getHeader(API_KEY_HEADER);
			String retailer = request.getHeader(RETAILER_HEADER);

			if (apiKey == null || apiKey.isBlank()) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Missing API Key");
				return;
			}

			// Find retailer with matching API key
			Optional<Retailer> optionalRetailer = retailerRepository.findByRetailType(RetailerType.fromName(retailer));

			if (optionalRetailer.isEmpty()) {
				logger.warn("Trace Id: {} - Retailer not found for '{}'", traceId, retailer);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Invalid API Key");
				return;
			}
			
	        Retailer retailerObj = optionalRetailer.get();
	        if (!passwordEncoder.matches(apiKey, retailerObj.getApiKey())) {
				logger.warn("Trace Id: {} - API key does not match for retailer '{}'", traceId, retailer);
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write("Invalid API Key");
	            return;
	        }

			filterChain.doFilter(request, response);
		} finally {
			MDC.clear();  // Important to prevent leakage across threads
		}


	}
}
