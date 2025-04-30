package com.allica.allica_bank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.allica.allica_bank.entity.Customer;
import com.allica.allica_bank.entity.Retailer;
import com.allica.allica_bank.enums.RetailerType;
import com.allica.allica_bank.model.customer.CustomerRequestDTO;
import com.allica.allica_bank.model.customer.CustomerResponseDTO;
import com.allica.allica_bank.repository.CustomerRepository;
import com.allica.allica_bank.repository.RetailerRepository;

/**
 * Integration tests for {@link CustomerService}.
 */
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    private Retailer testRetailer;

    /**
     * Sets up a retailer before each test case and clears repositories.
     */
    @BeforeEach
    void setup() {
        customerRepository.deleteAll();
        retailerRepository.deleteAll();

        testRetailer = new Retailer();
        testRetailer.setType(RetailerType.AMAZON);
        testRetailer.setName("Test Retailer");
        testRetailer.setApiKey(new BCryptPasswordEncoder().encode("password"));
        testRetailer = retailerRepository.save(testRetailer);
    }

    /**
     * Helper method to create a sample customer request DTO.
     */
    private CustomerRequestDTO buildCustomerRequestDTO() {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setFirstName("Alice");
        dto.setLastName("Smith");
        dto.setDob("1990-01-01");
        dto.setLoginName("alice.smith");
        return dto;
    }

    /**
     * Test creating a customer should persist successfully.
     */
    @Test
    void createCustomer_ShouldSucceed() {
        CustomerRequestDTO request = buildCustomerRequestDTO();

        CustomerResponseDTO response = customerService.createCustomer(request, "AMAZON");

        assertThat(response).isNotNull();
        assertThat(response.getFirstName()).isEqualTo("Alice");

        Customer saved = customerRepository.findById(response.getId()).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getRetailer().getType()).isEqualTo(RetailerType.AMAZON);
    }

    /**
     * Test retrieving a customer by ID should return the correct customer.
     */
    @Test
    void getCustomer_ShouldReturnCorrectCustomer() {
        CustomerRequestDTO request = buildCustomerRequestDTO();
        CustomerResponseDTO created = customerService.createCustomer(request, "AMAZON");

        CustomerResponseDTO fetched = customerService.getCustomer(created.getId(), "AMAZON");

        assertThat(fetched.getFirstName()).isEqualTo("Alice");
        assertThat(fetched.getLoginName()).isEqualTo("alice.smith");
    }

    /**
     * Test retrieving a non-existent customer should throw an exception.
     */
    @Test
    void getCustomer_ShouldThrowIfNotFound() {
        assertThatThrownBy(() -> customerService.getCustomer(999L, "AMAZON"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Customer Information is not found");
    }

    /**
     * Test updating a customer should update the fields correctly.
     */
    @Test
    void updateCustomer_ShouldUpdateFields() {
        CustomerRequestDTO request = buildCustomerRequestDTO();
        CustomerResponseDTO created = customerService.createCustomer(request, "AMAZON");

        CustomerRequestDTO updateRequest = new CustomerRequestDTO();
        updateRequest.setFirstName("Bob");
        updateRequest.setLastName("Jones");
        updateRequest.setDob("1985-02-05");
        updateRequest.setLoginName("bob.jones");

        CustomerResponseDTO updated = customerService.updateCustomer(created.getId(), "AMAZON", updateRequest);

        assertThat(updated.getFirstName()).isEqualTo("Bob");
        assertThat(updated.getLoginName()).isEqualTo("bob.jones");
    }

    /**
     * Test deleting a customer should remove it from the repository.
     */
    @Test
    void deleteCustomer_ShouldRemoveEntity() {
        CustomerRequestDTO request = buildCustomerRequestDTO();
        CustomerResponseDTO created = customerService.createCustomer(request, "AMAZON");

        customerService.deleteCustomer(created.getId(), "AMAZON");

        assertThat(customerRepository.findById(created.getId())).isEmpty();
    }

    /**
     * Test deleting a non-existent customer should throw an exception.
     */
    @Test
    void deleteCustomer_ShouldThrowIfCustomerDoesNotExist() {
        assertThatThrownBy(() -> customerService.deleteCustomer(1000L, "AMAZON"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Customer Information is not found");
    }
}
