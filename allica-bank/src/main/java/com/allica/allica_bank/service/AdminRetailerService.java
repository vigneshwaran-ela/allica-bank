package com.allica.allica_bank.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.allica.allica_bank.entity.Customer;
import com.allica.allica_bank.entity.Retailer;
import com.allica.allica_bank.model.retailer.RetailerRequestDTO;
import com.allica.allica_bank.model.retailer.RetailerResponseDTO;
import com.allica.allica_bank.repository.CustomerRepository;
import com.allica.allica_bank.repository.RetailerRepository;


/**
 * Service class that handles operations for managing retailers in the admin context.
 * Supports create, retrieve, update, and delete (CRUD) operations.
 */
@Service
public class AdminRetailerService {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminRetailerService.class);
	
	@Autowired
	private RetailerRepository retailerRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	 /**
     * Creates a new retailer if a retailer with the same name does not already exist.
     *
     * @param dto the request DTO containing retailer details
     * @return a response DTO with saved retailer information
     * @throws RuntimeException if a retailer with the same name already exists
     */
    public RetailerResponseDTO createRetailer(RetailerRequestDTO dto) {
    	
        logger.info("Attempting to create retailer with name: {}", dto.getName());
    	
    	Optional<Retailer> retailerOptional = this.retailerRepository.findByName(dto.getName());
    	
    	if(retailerOptional.isPresent()) {
    		logger.warn("Retailer is already present, please try with some other retailer name");
    		throw new RuntimeException("Retailer is already present, please try something else " + dto.getName());
    	}
        Retailer retailer = new Retailer();
        BeanUtils.copyProperties(dto, retailer);
        retailer.setApiKey(passwordEncoder.encode(dto.getApiKey()));
        Retailer saved = this.retailerRepository.save(retailer);
        logger.info("Retailer created successfully with ID: {}", saved.getId());        
        return toResponseDTO(saved);
    }
    
    /**
     * Retrieves a retailer by its ID.
     *
     * @param id the retailer ID
     * @return a response DTO with retailer details
     * @throws RuntimeException if the retailer is not found
     */
    public RetailerResponseDTO getRetailerById(Long id) {
        logger.info("Fetching retailer with ID: {}", id);
        Retailer retailer = retailerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Retailer not found with id: " + id));
        return toResponseDTO(retailer);
    }
    
    /**
     * Retrieves all retailers in the system.
     *
     * @return a list of response DTOs for all retailers
     */
    public List<RetailerResponseDTO> getAllRetailers() {
        logger.info("Fetching all retailers");
        return retailerRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    
    /**
     * Updates an existing retailer by ID.
     *
     * @param id  the ID of the retailer to update
     * @param dto the updated retailer data
     * @return a response DTO with updated retailer information
     * @throws RuntimeException if the retailer is not found
     */
    public RetailerResponseDTO updateRetailer(Long id, RetailerRequestDTO dto) {
        logger.info("Attempting to update retailer with ID: {}", id);    	
        Retailer retailer = retailerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Retailer not found with id: " + id));

        retailer.setName(dto.getName());

        if (dto.getApiKey() != null && !dto.getApiKey().isBlank()) {
            retailer.setApiKey(passwordEncoder.encode(dto.getApiKey()));
        }

        Retailer updated = retailerRepository.save(retailer);
        logger.info("Retailer updated successfully with ID: {}", updated.getId());        
        return toResponseDTO(updated);
    }
    
    
    /**
     * Deletes a retailer by ID if no customers reference it.
     *
     * @param id the ID of the retailer to delete
     * @throws RuntimeException if the retailer is not found or if it is referenced by customers
     */
    public void deleteRetailer(Long id) {
        logger.info("Attempting to delete retailer with ID: {}", id);
    	
        Retailer retailer = retailerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Retailer not found with id: " + id));
        
        List<Customer> customer = this.customerRepository.findByRetailerId(retailer.getId());
        
        if(Objects.nonNull(customer) && !customer.isEmpty()) {
            logger.warn("Retailer deletion blocked due to existing customer references, retailer ID: {}", id);
        	throw new RuntimeException("One or more customer records still reference it through the retailer_id foreign key: " + id);
        }
        retailerRepository.deleteById(id);
        logger.info("Retailer deleted successfully with ID: {}", id);
    }
    
    
    /**
     * Converts a {@link Retailer} entity to a {@link RetailerResponseDTO}.
     *
     * @param retailer the entity to convert
     * @return the response DTO
     */
    private RetailerResponseDTO toResponseDTO(Retailer retailer) {
        RetailerResponseDTO dto = new RetailerResponseDTO();
        BeanUtils.copyProperties(retailer, dto);
        return dto;
    }
}
