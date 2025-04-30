package com.allica.allica_bank.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.allica.allica_bank.enums.RetailerType;
import com.allica.allica_bank.model.customer.CustomerRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Integration tests for CustomerController endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/customer";
    private static final String RETAILER_HEADER = "X-RETAILER";
    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String RETAILER_NAME = "AMAZON";
    private static final String API_KEY = "APIKEY_AMAZON_123";

    /**
     * Tests successful creation of a customer with unique login name.
     */
    @Test
    @Order(1)
    void createCustomer_withValidInput_shouldReturnCreated() throws Exception {
        CustomerRequestDTO requestDTO = createCustomerRequestDto("unique_login");

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .header(RETAILER_HEADER, RETAILER_NAME)
                        .header(API_KEY_HEADER, API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loginName").value("unique_login"))
                .andExpect(jsonPath("$.firstName").value("Vignesh12"))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Tests customer creation fails if login name already exists.
     */
    @Test
    @Order(2)
    void createCustomer_withDuplicateLogin_shouldReturnBadRequest() throws Exception {
        CustomerRequestDTO requestDTO = createCustomerRequestDto("unique_login");

        mockMvc.perform(post(BASE_URL)
                        .header(RETAILER_HEADER, RETAILER_NAME)
                        .header(API_KEY_HEADER, API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Login name")))
                .andExpect(content().string(Matchers.containsString("already in use")));
    }

    /**
     * Tests missing headers result in unauthorized error.
     */
    @Test
    void createCustomer_withoutHeaders_shouldReturnUnauthorized() throws Exception {
        CustomerRequestDTO requestDTO = createCustomerRequestDto("r_vignesh");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Tests successful retrieval of existing customer by ID.
     */
    @Test
    @Order(3)
    void getCustomer_withValidId_shouldReturnCustomer() throws Exception {
        mockMvc.perform(get(BASE_URL + "/100")
                        .header(RETAILER_HEADER, RETAILER_NAME)
                        .header(API_KEY_HEADER, API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginName").value("unique_login"))
                .andExpect(jsonPath("$.firstName").value("Vignesh12"));
    }

    /**
     * Tests retrieval with non-existent customer ID returns error.
     */
    @Test
    void getCustomer_withInvalidId_shouldReturnServerError() throws Exception {
        mockMvc.perform(get(BASE_URL + "/99")
                        .header(RETAILER_HEADER, RETAILER_NAME)
                        .header(API_KEY_HEADER, API_KEY))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(Matchers.containsString("not found")));
    }

    /**
     * Tests successful update of a customer.
     */
    @Test
    @Order(4)
    void updateCustomer_withValidId_shouldUpdateCustomer() throws Exception {
        CustomerRequestDTO requestDTO = createCustomerRequestDto("1vignesh_zoo");

        mockMvc.perform(put(BASE_URL + "/100")
                        .header(RETAILER_HEADER, RETAILER_NAME)
                        .header(API_KEY_HEADER, API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Vignesh12"));
    }

    /**
     * Tests update fails for non-existent customer ID.
     */
    @Test
    void updateCustomer_withInvalidId_shouldReturnServerError() throws Exception {
        CustomerRequestDTO requestDTO = createCustomerRequestDto("vignesh_raj");

        mockMvc.perform(put(BASE_URL + "/99")
                        .header(RETAILER_HEADER, RETAILER_NAME)
                        .header(API_KEY_HEADER, API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(Matchers.containsString("not found")));
    }

    /**
     * Tests successful deletion of a customer.
     */
    @Test
    @Order(5)
    void deleteCustomer_withValidId_shouldReturnSuccess() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/100")
                        .header(RETAILER_HEADER, RETAILER_NAME)
                        .header(API_KEY_HEADER, API_KEY))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer deleted successfully"));
    }

    /**
     * Tests deletion with non-existent customer ID returns error.
     */
    @Test
    void deleteCustomer_withInvalidId_shouldReturnServerError() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/99")
                        .header(RETAILER_HEADER, RETAILER_NAME)
                        .header(API_KEY_HEADER, API_KEY))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(Matchers.containsString("not found")));
    }

    /**
     * Tests failure with invalid API key.
     */
    @Test
    void deleteCustomer_withInvalidApiKey_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/99")
                        .header(RETAILER_HEADER, RetailerType.AMAZON)
                        .header(API_KEY_HEADER, "no_api_key"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(Matchers.containsString("Invalid API Key")));
    }

    /**
     * Tests failure with invalid retailer name.
     */
    @Test
    void deleteCustomer_withRetailerDataMissing_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/99")
                        .header(RETAILER_HEADER, RetailerType.ZEPTO)
                        .header(API_KEY_HEADER, API_KEY))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(Matchers.containsString("Invalid Retailer name or Reatiler data not present")));
    }

    /**
     * Tests failure with unknown retailer name.
     */
    @Test
    void deleteCustomer_withUnknownRetailerName_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/99")
                        .header(RETAILER_HEADER, "no_retailer_name")
                        .header(API_KEY_HEADER, API_KEY))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(Matchers.containsString("Invalid")));
    }

    // ---------- Helper method ----------
    private CustomerRequestDTO createCustomerRequestDto(String loginName) {
        CustomerRequestDTO requestDTO = new CustomerRequestDTO();
        requestDTO.setFirstName("Vignesh12");
        requestDTO.setDob("1993-09-09");
        requestDTO.setLoginName(loginName);
        return requestDTO;
    }
}
