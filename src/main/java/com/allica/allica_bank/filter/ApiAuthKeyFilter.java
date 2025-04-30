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

/**
 * Filter that performs API key authentication for requests targeting /api/v1/customer endpoints.
 * It extracts the API key and retailer name from the headers, verifies them, and sets up trace logging.
 */
@Component
public class ApiAuthKeyFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiAuthKeyFilter.class);
	private static final String API_KEY_HEADER = "X-API-KEY";
	private static final String RETAILER_HEADER = "X-RETAILER";

	@Autowired
	private RetailerRepository retailerRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

    /**
     * Filters incoming HTTP requests and validates the API key for customer-related endpoints.
     */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			String traceId = UUID.randomUUID().toString();
			MDC.put("traceId", traceId);
	        String path = request.getRequestURI();
	        if(!path.startsWith("/h2-console"))
	        	logger.info("Processing for request to path: {}", path);
            // Skip validation for non-customer APIs
	        if (!path.startsWith("/api/v1/customer")) {
	            filterChain.doFilter(request, response); // skip API key check
	            return;
	        }

			String apiKey = request.getHeader(API_KEY_HEADER);
			String retailer = request.getHeader(RETAILER_HEADER);

			if (apiKey == null || apiKey.isBlank()) {
                logger.warn("Missing Retailer header in request");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Missing API Key");
				return;
			}

			// Find retailer with matching API key
			Optional<Retailer> optionalRetailer = retailerRepository.findByType(RetailerType.fromName(retailer));
			
			if(optionalRetailer.isEmpty()) {
                logger.warn("Invalid retailer name: {}", retailer);
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write("Invalid Retailer name or Reatiler data not present");
	            return;
			}
			
	        Retailer retailerObj = optionalRetailer.get();
	        if (!passwordEncoder.matches(apiKey, retailerObj.getApiKey())) {
				logger.warn("API key does not match for retailer '{}'", retailer);
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write("Invalid API Key");
	            return;
	        }

			filterChain.doFilter(request, response);
		} catch (Exception e) {
			logger.error("Error while processing authentication for api key and retailer ", e);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid Retailer name or API Key");
			return;
		} finally {
			MDC.clear();  // Important to prevent leakage across threads
		}


	}
}
