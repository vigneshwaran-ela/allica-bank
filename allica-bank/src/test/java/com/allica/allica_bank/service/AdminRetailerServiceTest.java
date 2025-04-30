package com.allica.allica_bank.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    @Order(1)
    void testCreateRetailer() {
        // Create a new retailer
        RetailerRequestDTO dto = new RetailerRequestDTO();
        dto.setName("TestRetailer");
        dto.setApiKey("SECRET123");
        dto.setRetailType(RetailerType.ZEPTO);

        RetailerResponseDTO response = adminRetailerService.createRetailer(dto);

        assertNotNull(response.getId());
        assertEquals("TestRetailer", response.getName());

        createdRetailerId = response.getId(); // store for later tests
    }
    
    @Test
    @Order(2)
    void testRetailer_Exist() {
        // Create a new retailer
        RetailerRequestDTO dto = new RetailerRequestDTO();
        dto.setName("TestRetailer");
        dto.setApiKey("SECRET123");
        dto.setRetailType(RetailerType.ZEPTO);

        // When + Then: expect RuntimeException
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            adminRetailerService.createRetailer(dto);
        });

        assertTrue(ex.getMessage().contains("Retailer is already present"));
    }
    
    

    @Test
    @Order(2)
    void testGetRetailerById() {
        RetailerResponseDTO retailer = adminRetailerService.getRetailerById(createdRetailerId);
        assertEquals("TestRetailer", retailer.getName());
    }

    @Test
    @Order(3)
    void testGetAllRetailers() {
        List<RetailerResponseDTO> allRetailers = adminRetailerService.getAllRetailers();
        assertTrue(allRetailers.size() >= 1);
    }

    @Test
    @Order(4)
    void testUpdateRetailer() {
        RetailerRequestDTO updateDTO = new RetailerRequestDTO();
        updateDTO.setName("UpdatedRetailer");
        updateDTO.setApiKey("NEW_SECRET");
        updateDTO.setRetailType(RetailerType.FLIPKART);

        RetailerResponseDTO updated = adminRetailerService.updateRetailer(createdRetailerId, updateDTO);
        assertEquals("UpdatedRetailer", updated.getName());

        // Verify the API key is encrypted
        Retailer entity = retailerRepository.findById(createdRetailerId).orElseThrow();
        assertNotEquals("NEW_SECRET", entity.getApiKey());
        assertTrue(passwordEncoder.matches("NEW_SECRET", entity.getApiKey()));
    }

    @Test
    @Order(5)
    void testDeleteRetailerSuccess() {
        // Ensure no customer is referencing this retailer
        assertTrue(customerRepository.findByRetailerId(createdRetailerId).isEmpty());

        // Attempt deletion
        assertDoesNotThrow(() -> adminRetailerService.deleteRetailer(createdRetailerId));

        // Ensure it is deleted
        assertFalse(retailerRepository.findById(createdRetailerId).isPresent());
    }

    @Test
    @Order(6)
    void testDeleteRetailerFailsWhenCustomerExists() {
        // Create a retailer and link a customer manually for test (setup needs to exist)
        Retailer retailer = new Retailer();
        retailer.setName("LinkedRetailer");
        retailer.setApiKey(passwordEncoder.encode("KEY"));
        retailer.setRetailType(RetailerType.AMAZON);
        Retailer saved = retailerRepository.save(retailer);

        Long linkedRetailerId = saved.getId();

        // Create a customer linked to this retailer using direct SQL or repository
        customerRepository.save(TestDataHelper.createCustomer("linked_user", linkedRetailerId));

        // Expect delete to fail
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminRetailerService.deleteRetailer(linkedRetailerId));

        assertTrue(ex.getMessage().contains("customer records"));
    }
}