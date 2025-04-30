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

    @BeforeEach
    void setup() {
        customerRepository.deleteAll();
        retailerRepository.deleteAll();

        testRetailer = new Retailer();
        testRetailer.setRetailType(RetailerType.AMAZON);  // Ensure this matches your enum
        testRetailer.setName("Test Retailer");
        testRetailer.setApiKey(new BCryptPasswordEncoder().encode("password"));
        testRetailer = retailerRepository.save(testRetailer);
    }

    private CustomerRequestDTO buildCustomerRequestDTO() {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setFirstName("Alice");
        dto.setLastName("Smith");
        dto.setDob("1990-01-01");
        dto.setLoginName("alice.smith");
        return dto;
    }

    @Test
    void createCustomer_ShouldSucceed() {
        CustomerRequestDTO request = buildCustomerRequestDTO();

        CustomerResponseDTO response = customerService.createCustomer(request, "AMAZON");

        assertThat(response).isNotNull();
        assertThat(response.getFirstName()).isEqualTo("Alice");

        Customer saved = customerRepository.findById(response.getId()).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getRetailer().getRetailType()).isEqualTo(RetailerType.AMAZON);
    }

    @Test
    void getCustomer_ShouldReturnCorrectCustomer() {
        // Create first
        CustomerRequestDTO request = buildCustomerRequestDTO();
        CustomerResponseDTO created = customerService.createCustomer(request, "AMAZON");

        CustomerResponseDTO fetched = customerService.getCustomer(created.getId(), "AMAZON");

        assertThat(fetched.getFirstName()).isEqualTo("Alice");
        assertThat(fetched.getLoginName()).isEqualTo("alice.smith");
    }

    @Test
    void getCustomer_ShouldThrowIfNotFound() {
        assertThatThrownBy(() -> customerService.getCustomer(999L, "AMAZON"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Customer Information is not found");
    }

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

    @Test
    void deleteCustomer_ShouldRemoveEntity() {
        CustomerRequestDTO request = buildCustomerRequestDTO();
        CustomerResponseDTO created = customerService.createCustomer(request, "AMAZON");

        customerService.deleteCustomer(created.getId(), "AMAZON");

        assertThat(customerRepository.findById(created.getId())).isEmpty();
    }

    @Test
    void deleteCustomer_ShouldThrowIfCustomerDoesNotExist() {
        assertThatThrownBy(() -> customerService.deleteCustomer(1000L, "AMAZON"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Customer Information is not found");
    }
}