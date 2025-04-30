package com.allica.allica_bank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allica.allica_bank.model.customer.CustomerRequestDTO;
import com.allica.allica_bank.model.customer.CustomerResponseDTO;
import com.allica.allica_bank.service.CustomerService;

import jakarta.validation.Valid;

/**
 * Controller that handles customer-related operations for specific retailers.
 * Endpoints include create, retrieve, update, and delete functionality.
 */
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;

    /**
     * Create a new customer for the specified retailer.
     *
     * @param customerRequestDTO The customer details to create.
     * @param retailerName The name of the retailer (from request header).
     * @return The created customer with a 201 status.
     */
	@PostMapping
	public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody @Valid CustomerRequestDTO customerRequestDTO,
			@RequestHeader("X-RETAILER") String retailerName) {
		logger.info("Creating customer for retailer: {}", retailerName);
		CustomerResponseDTO response = this.customerService.createCustomer(customerRequestDTO, retailerName);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

    /**
     * Retrieve a customer by ID under a specific retailer.
     *
     * @param id The customer ID.
     * @param retailerName The name of the retailer (from request header).
     * @return The customer information if found.
     */
	@GetMapping("/{id}")
	public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable Long id,
			@RequestHeader("X-RETAILER") String retailerName) {
		logger.info("Fetching customer with ID: {} for retailer: {}", id, retailerName);
		CustomerResponseDTO customer = customerService.getCustomer(id, retailerName);
		return ResponseEntity.ok(customer);
	}

    /**
     * Update an existing customer by ID for the given retailer.
     *
     * @param id The customer ID.
     * @param customerRequestDTO The updated customer information.
     * @param retailerName The name of the retailer (from request header).
     * @return The updated customer details.
     */
	@PutMapping("/{id}")
	public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id,
			@RequestBody @Valid CustomerRequestDTO customerRequestDTO,
			@RequestHeader("X-RETAILER") String retailerName) {
		logger.info("Updating customer with ID: {} for retailer: {}", id, retailerName);
		CustomerResponseDTO updated = customerService.updateCustomer(id, retailerName, customerRequestDTO);
		return ResponseEntity.ok(updated);
	}

    /**
     * Delete a customer by ID under a given retailer.
     *
     * @param id The customer ID.
     * @param retailerName The name of the retailer (from request header).
     * @return A success message upon deletion.
     */ 
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Long id,
			@RequestHeader("X-RETAILER") String retailerName) {
		logger.info("Deleting customer with ID: {} for retailer: {}", id, retailerName);
		customerService.deleteCustomer(id, retailerName);
		return ResponseEntity.ok("Customer deleted successfully");
	}
}
