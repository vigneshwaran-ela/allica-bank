package com.allica.allica_bank.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allica.allica_bank.model.retailer.RetailerRequestDTO;
import com.allica.allica_bank.model.retailer.RetailerResponseDTO;
import com.allica.allica_bank.service.AdminRetailerService;

import jakarta.validation.Valid;

/**
 * Controller to manage admin-level operations for Retailers.
 * Exposes CRUD endpoints under /api/v1/admin/retailer.
 */
@RestController
@RequestMapping("/api/v1/admin/retailer")
public class AdminRetailerController {
	
    private static final Logger logger = LoggerFactory.getLogger(AdminRetailerController.class);	
	
	@Autowired
	private AdminRetailerService adminRetailerService;

    /**
     * Creates a new retailer.
     *
     * @param retailerRequestDTO DTO containing retailer details
     * @return created Retailer details
     */
    @PostMapping
    public RetailerResponseDTO createRetailer(@RequestBody @Valid RetailerRequestDTO retailerRequestDTO) {
        logger.info("Creating new retailer: {}", retailerRequestDTO.getName());
        return adminRetailerService.createRetailer(retailerRequestDTO);
    }

    /**
     * Retrieves a retailer by ID.
     *
     * @param id Retailer ID
     * @return Retailer details
     */
    @GetMapping("/{id}")
    public RetailerResponseDTO getRetailer(@PathVariable Long id) {
        logger.info("Fetching retailer with ID: {}", id);
        return adminRetailerService.getRetailerById(id);
    }

    /**
     * Retrieves all retailers.
     *
     * @return list of all Retailers
     */
    @GetMapping
    public List<RetailerResponseDTO> getAllRetailers() {
        logger.info("Fetching all retailers");
        return adminRetailerService.getAllRetailers();
    }
    
    /**
     * Updates an existing retailer by ID.
     *
     * @param id Retailer ID
     * @param retailerRequestDTO DTO with updated retailer details
     * @return updated Retailer details
     */
    @PutMapping("/{id}")
    public RetailerResponseDTO updateRetailer(@PathVariable Long id, @RequestBody @Valid RetailerRequestDTO retailerRequestDTO) {
        logger.info("Updating retailer with ID: {}", id);
    	return adminRetailerService.updateRetailer(id, retailerRequestDTO);
    }

    /**
     * Deletes a retailer by ID.
     *
     * @param id Retailer ID
     * @return confirmation message
     */
    @DeleteMapping("/{id}")
    public String deleteRetailer(@PathVariable Long id) {
        logger.info("Deleting retailer with ID: {}", id);    	
        adminRetailerService.deleteRetailer(id);
        return "Retailer deleted successfully";
    }
}
