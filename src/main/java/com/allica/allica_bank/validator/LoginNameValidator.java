package com.allica.allica_bank.validator;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.allica.allica_bank.annotation.UniqueLoginName;
import com.allica.allica_bank.entity.Customer;
import com.allica.allica_bank.entity.Retailer;
import com.allica.allica_bank.enums.RetailerType;
import com.allica.allica_bank.repository.CustomerRepository;
import com.allica.allica_bank.repository.RetailerRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class LoginNameValidator implements ConstraintValidator<UniqueLoginName, String> {

	Logger logger = LoggerFactory.getLogger(LoginNameValidator.class);

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private RetailerRepository retailerRepository;

	@Override
	public boolean isValid(String loginName, ConstraintValidatorContext context) {

		HttpServletRequest request = getCurrentHttpRequest();

		if (request != null) {
			String retailerName = request.getHeader("X-RETAILER");

			Optional<Retailer> retailer = this.retailerRepository.findByRetailType(RetailerType.fromName(retailerName));

			Optional<Customer> customer = this.customerRepository.findByRetailerIdAndLoginName(retailer.get().getId(),
					loginName);
			if(customer.isPresent()) {
			    logger.warn("Login name '{}' already exists for retailer '{}'", loginName, retailerName);
			    return false;
			}
			return customer.isEmpty();
		}
		return false;
	}

	private HttpServletRequest getCurrentHttpRequest() {
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		if (attrs instanceof ServletRequestAttributes) {
			return ((ServletRequestAttributes) attrs).getRequest();
		}
		return null;
	}
}
