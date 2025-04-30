package com.allica.allica_bank.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import com.allica.allica_bank.entity.Customer;
import com.allica.allica_bank.entity.Retailer;
import com.allica.allica_bank.enums.RetailerType;
import com.allica.allica_bank.model.customer.CustomerRequestDTO;
import com.allica.allica_bank.model.customer.CustomerResponseDTO;
import com.allica.allica_bank.repository.CustomerRepository;
import com.allica.allica_bank.repository.RetailerRepository;

/**
 * Service class to handle business logic for managing customers.
 */
@Service
public class CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private RetailerRepository retailerRepository;

	/**
	 * Creates a new customer for the given retailer.
	 *
	 * @param customerRequestDTO Customer data
	 * @param retailerName Name of the retailer (as string)
	 * @return Response DTO with persisted customer information
	 */
	public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO, String retailerName) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerRequestDTO, customer);
		Optional<Retailer> retailer = this.retailerRepository.findByType(RetailerType.fromName(retailerName));
		customer.setRetailer(retailer.get());
		Customer customerEntity = this.customerRepository.save(customer);
		logger.info("Customer information is created for the id: {} and first name: {}",  customer.getId(), customer.getFirstName());		
		return convertEntityToResponseDto(customerEntity);
	}

	/**
	 * Retrieves a customer by ID and retailer.
	 */
	public CustomerResponseDTO getCustomer(Long id, String retailerName) {
		Customer customer = getCustomerInfo(retailerName, id);
	    logger.info("Customer found with ID: {}", id);
		return convertEntityToResponseDto(customer);
	}

	/**
	 * Deletes a customer by ID and retailer.
	 */
	public void deleteCustomer(Long id, String retailerName) {	
		Customer customer = getCustomerInfo(retailerName, id);
		this.customerRepository.delete(customer);
		logger.info("Customer information is deleted for the id: {}", id);
	}

	/**
	 * Updates a customer's details.
	 */
	public CustomerResponseDTO updateCustomer(Long id, String retailerName, CustomerRequestDTO customerRequestDTO) {

		Customer customer = getCustomerInfo(retailerName, id);
		
		// Update customer fields
		customer.setFirstName(customerRequestDTO.getFirstName());
		customer.setLastName(customerRequestDTO.getLastName());
		customer.setDob(customerRequestDTO.getDob());
		customer.setLoginName(customerRequestDTO.getLoginName());
		Customer updatedCustomer;
		try {
		    updatedCustomer = customerRepository.save(customer);
		} catch (TransactionSystemException ex) {
		    Throwable cause = ex.getRootCause();
		    logger.error("Transaction commit failed: {}", cause != null ? cause.getMessage() : "unknown", ex);
		    throw ex; 
		}

		// Map entity to response DTO
		CustomerResponseDTO responseDTO = new CustomerResponseDTO();
		BeanUtils.copyProperties(updatedCustomer, responseDTO);
		responseDTO.setRetailerId(updatedCustomer.getRetailer().getId()); // set manually if needed
		logger.info("Customer information is updated for the id: {} and first name: {}", customer.getId(), customer.getFirstName());
		return responseDTO;
	}
	
	/**
	 * Retrieves the customer entity from the DB after verifying the retailer association.
	 */
	private Customer getCustomerInfo(String retailerName, Long id) {
		// This will never be empty because it's validated filter layer
		Optional<Retailer> retailer = this.retailerRepository.findByType(RetailerType.fromName(retailerName));

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

	/**
	 * Converts a Customer entity to a CustomerResponseDTO.
	 */
	private CustomerResponseDTO convertEntityToResponseDto(Customer customer) {
		CustomerResponseDTO responseDTO = new CustomerResponseDTO();
		BeanUtils.copyProperties(customer, responseDTO);
		return responseDTO;
	}
}
