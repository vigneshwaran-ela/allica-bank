package com.allica.allica_bank.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.allica.allica_bank.entity.Retailer;
import com.allica.allica_bank.enums.RetailerType;
import com.allica.allica_bank.helper.TestDataHelper;
import com.allica.allica_bank.model.retailer.RetailerRequestDTO;
import com.allica.allica_bank.model.retailer.RetailerResponseDTO;
import com.allica.allica_bank.repository.CustomerRepository;
import com.allica.allica_bank.repository.RetailerRepository;

/**
 * Integration tests for {@link AdminRetailerService}.
 */
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminRetailerServiceTest {

    @Autowired
    private AdminRetailerService adminRetailerService;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static Long createdRetailerId;

    /**
     * Helper method to build a {@link RetailerRequestDTO}.
     */
    private RetailerRequestDTO buildRetailerDTO(String name, String apiKey, RetailerType type) {
        RetailerRequestDTO dto = new RetailerRequestDTO();
        dto.setName(name);
        dto.setApiKey(apiKey);
        dto.setType(type);
        return dto;
    }

    /**
     * Test creating a retailer successfully.
     */
    @Test
    @Order(1)
    void createRetailer_ShouldSucceed() {
        RetailerRequestDTO dto = buildRetailerDTO("TestRetailer", "SECRET123", RetailerType.ZEPTO);
        RetailerResponseDTO response = adminRetailerService.createRetailer(dto);

        assertNotNull(response.getId());
        assertEquals("TestRetailer", response.getName());

        createdRetailerId = response.getId();
    }

    /**
     * Test creating a retailer that already exists should fail.
     */
    @Test
    @Order(2)
    void createRetailer_ShouldFailIfAlreadyExists() {
        RetailerRequestDTO dto = buildRetailerDTO("TestRetailer", "SECRET123", RetailerType.ZEPTO);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> adminRetailerService.createRetailer(dto));
        assertTrue(ex.getMessage().contains("Retailer is already present"));
    }

    /**
     * Test retrieving retailer by ID.
     */
    @Test
    @Order(3)
    void getRetailerById_ShouldReturnRetailer() {
        RetailerResponseDTO retailer = adminRetailerService.getRetailerById(createdRetailerId);
        assertEquals("TestRetailer", retailer.getName());
    }

    /**
     * Test retrieving all retailers.
     */
    @Test
    @Order(4)
    void getAllRetailers_ShouldReturnAtLeastOne() {
        List<RetailerResponseDTO> allRetailers = adminRetailerService.getAllRetailers();
        assertTrue(allRetailers.size() >= 1);
    }

    /**
     * Test updating an existing retailer.
     */
    @Test
    @Order(5)
    void updateRetailer_ShouldUpdateFields() {
        RetailerRequestDTO updateDTO = buildRetailerDTO("UpdatedRetailer", "NEW_SECRET", RetailerType.FLIPKART);
        RetailerResponseDTO updated = adminRetailerService.updateRetailer(createdRetailerId, updateDTO);
        assertEquals("UpdatedRetailer", updated.getName());

        Retailer entity = retailerRepository.findById(createdRetailerId).orElseThrow();
        assertNotEquals("NEW_SECRET", entity.getApiKey());
        assertTrue(passwordEncoder.matches("NEW_SECRET", entity.getApiKey()));
    }

    /**
     * Test deleting a retailer with no linked customers should succeed.
     */
    @Test
    @Order(6)
    void deleteRetailer_ShouldSucceedWhenNoCustomersLinked() {
        assertTrue(customerRepository.findByRetailerId(createdRetailerId).isEmpty());
        assertDoesNotThrow(() -> adminRetailerService.deleteRetailer(createdRetailerId));
        assertFalse(retailerRepository.findById(createdRetailerId).isPresent());
    }

    /**
     * Test deleting a retailer that has linked customers should fail.
     */
    @Test
    @Order(7)
    void deleteRetailer_ShouldFailWhenCustomersExist() {
        Retailer retailer = new Retailer();
        retailer.setName("LinkedRetailer");
        retailer.setApiKey(passwordEncoder.encode("KEY"));
        retailer.setType(RetailerType.AMAZON);
        Retailer saved = retailerRepository.save(retailer);

        Long linkedRetailerId = saved.getId();
        customerRepository.save(TestDataHelper.createCustomer("linked_user", linkedRetailerId));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminRetailerService.deleteRetailer(linkedRetailerId));
        assertTrue(ex.getMessage().contains("customer records"));
    }

    /**
     * Test retrieving a non-existent retailer by ID should throw.
     */
    @Test
    @Order(8)
    void getRetailerById_ShouldThrowIfNotFound() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            adminRetailerService.getRetailerById(999999L)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("not found"));
    }

    /**
     * Test updating a non-existent retailer should throw.
     */
    @Test
    @Order(9)
    void updateRetailer_ShouldThrowIfNotFound() {
        RetailerRequestDTO dto = buildRetailerDTO("GhostRetailer", "ghost", RetailerType.AMAZON);
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            adminRetailerService.updateRetailer(999999L, dto)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("not found"));
    }
}