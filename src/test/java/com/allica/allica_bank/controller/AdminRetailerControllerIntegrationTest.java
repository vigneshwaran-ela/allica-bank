package com.allica.allica_bank.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.allica.allica_bank.enums.RetailerType;
import com.allica.allica_bank.model.retailer.RetailerRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Integration tests for {@link AdminRetailerController}.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminRetailerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long createdRetailerId;

    private final String BASE_URL = "/api/v1/admin/retailer";
    private final String ADMIN_USERNAME = "ups_admin";
    private final String ADMIN_PASSWORD = "password123";

    /**
     * Tests retrieval of all retailers, expecting at least 3 to be present.
     */
    @Test
    @Order(1)
    void getAllRetailers_shouldReturnListWithMinimumSize() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL)
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", greaterThanOrEqualTo(3)))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response from GET /admin/retailer: " + responseBody);
    }

    /**
     * Tests retrieval of retailer by ID (105), expecting the name to be "Walmart".
     */
    @Test
    @Order(2)
    void getRetailerById_withValidId_shouldReturnRetailerDetails() throws Exception {
        mockMvc.perform(get(BASE_URL + "/105")
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Walmart"));
    }

    /**
     * Tests creation of a new retailer with the name "Target".
     */
    @Test
    @Order(3)
    void createRetailer_withValidPayload_shouldPersistAndReturnRetailer() throws Exception {
        RetailerRequestDTO dto = new RetailerRequestDTO();
        dto.setName("Target");
        dto.setApiKey("APIKEY_TARGET_999");
        dto.setType(RetailerType.ZEPTO);

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Target"))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        createdRetailerId = jsonNode.get("id").asLong();
    }

    /**
     * Tests updating the retailer created in the previous step.
     */
    @Test
    @Order(4)
    void updateRetailer_withValidPayload_shouldModifyRetailerData() throws Exception {
        RetailerRequestDTO dto = new RetailerRequestDTO();
        dto.setName("Target - Updated");
        dto.setApiKey("NEW_APIKEY_TARGET");
        dto.setType(RetailerType.FLIPKART);

        mockMvc.perform(put(BASE_URL + "/" + createdRetailerId)
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Target - Updated"));
    }

    /**
     * Tests deletion of the previously created retailer.
     */
    @Test
    @Order(5)
    void deleteRetailer_withValidId_shouldRemoveRetailer() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + createdRetailerId)
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Retailer deleted successfully")));
    }

    /**
     * Tests behavior when using a non-existent admin login.
     */
    @Test
    @Order(6)
    void getRetailerById_withInvalidAdminCredentials_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get(BASE_URL + "/105")
                        .with(httpBasic("no_login_found", ADMIN_PASSWORD)))
                .andExpect(status().isUnauthorized());
    }
}
