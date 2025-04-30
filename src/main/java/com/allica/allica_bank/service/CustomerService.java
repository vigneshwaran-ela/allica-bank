package com.allica.allica_bank.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allica.allica_bank.entity.Customer;
import com.allica.allica_bank.entity.Retailer;
import com.allica.allica_bank.enums.RetailerType;
import com.allica.allica_bank.model.customer.CustomerRequestDTO;
import com.allica.allica_bank.model.customer.CustomerResponseDTO;
import com.allica.allica_bank.repository.CustomerRepository;
import com.allica.allica_bank.repository.RetailerRepository;

@Service
public class CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private RetailerRepository retailerRepository;

	public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO, String retailerName) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerRequestDTO, customer);
		Optional<Retailer> retailer = this.retailerRepository.findByRetailType(RetailerType.fromName(retailerName));
		customer.setRetailer(retailer.get());
		Customer customerEntity = this.customerRepository.save(customer);
		logger.info("Customer information is created for the id: {} and first name: {}",  customer.getId(), customer.getFirstName());		
		return convertEntityToResponseDto(customerEntity);
	}

	public CustomerResponseDTO getCustomer(Long id, String retailerName) {
		Customer customer = getCustomerInfo(retailerName, id);
	    logger.info("Customer found with ID: {}", id);
		return convertEntityToResponseDto(customer);
	}

	public void deleteCustomer(Long id, String retailerName) {	
		Customer customer = getCustomerInfo(retailerName, id);
		this.customerRepository.delete(customer);
		logger.info("Customer information is deleted for the id: {}", id);
	}

	public CustomerResponseDTO updateCustomer(Long id, String retailerName, CustomerRequestDTO customerRequestDTO) {

		Customer customer = getCustomerInfo(retailerName, id);
		
		// Update customer fields
		customer.setFirstName(customerRequestDTO.getFirstName());
		customer.setLastName(customerRequestDTO.getLastName());
		customer.setDob(customerRequestDTO.getDob());
		customer.setLoginName(customerRequestDTO.getLoginName());

		// Save updated customer
		Customer updatedCustomer = customerRepository.save(customer);

		// Map entity to response DTO
		CustomerResponseDTO responseDTO = new CustomerResponseDTO();
		BeanUtils.copyProperties(updatedCustomer, responseDTO);
		responseDTO.setRetailerId(updatedCustomer.getRetailer().getId()); // set manually if needed
		logger.info("Customer information is updated for the id: {} and first name: {}", customer.getId(), customer.getFirstName());
		return responseDTO;
	}
	
	private Customer getCustomerInfo(String retailerName, Long id) {
		// This will never be empty because it's validated filter layer
		Optional<Retailer> retailer = this.retailerRepository.findByRetailType(RetailerType.fromName(retailerName));

		// Fetch existing customer
		Optional<Customer> customerOptional = customerRepository.findByRetailerIdAndId(retailer.get().getId(), id);
		
		if (customerOptional.isEmpty()) {
			logger.warn("Customer information is not found for Id: {}", id);
			throw new RuntimeException("Customer Information is not found for ID: "+id);
		}
		Customer cusomter = customerOptional.get();
		cusomter.setRetailer(retailer.get());
		return cusomter;
	}

	private CustomerResponseDTO convertEntityToResponseDto(Customer customer) {
		CustomerResponseDTO responseDTO = new CustomerResponseDTO();
		BeanUtils.copyProperties(customer, responseDTO);
		return responseDTO;
	}
}
