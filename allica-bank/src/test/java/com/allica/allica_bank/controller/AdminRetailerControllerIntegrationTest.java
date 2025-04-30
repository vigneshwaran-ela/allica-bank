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

    @Test
    @Order(1)
    void testGetAllRetailers() throws Exception {
    	MvcResult result = mockMvc.perform(get(BASE_URL)
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", greaterThanOrEqualTo(3)))
                .andReturn();
        
        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response from GET /admin/retailer: " + responseBody);
    }

    @Test
    @Order(2)
    void testGetRetailerById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/105")
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Walmart"));
    }

    @Test
    @Order(3)
    void testCreateRetailer() throws Exception {
        RetailerRequestDTO dto = new RetailerRequestDTO();
        dto.setName("Target");
        dto.setApiKey("APIKEY_TARGET_999");
        dto.setRetailType(RetailerType.ZEPTO);

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Target"))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        createdRetailerId = jsonNode.get("id").asLong(); // capture ID for delete test
    }

    @Test
    @Order(4)
    void testUpdateRetailer() throws Exception {
        RetailerRequestDTO dto = new RetailerRequestDTO();
        dto.setName("Target - Updated");
        dto.setApiKey("NEW_APIKEY_TARGET");
        dto.setRetailType(RetailerType.FLIPKART);

        mockMvc.perform(put(BASE_URL + "/" + createdRetailerId)
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Target - Updated"));
    }

    @Test
    @Order(5)
    void testDeleteRetailer_Success() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + createdRetailerId)
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Retailer deleted successfully")));
    }
    
    @Test
    @Order(6)
    void testNoAdminLoginFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/105")
                        .with(httpBasic("no_login_found", ADMIN_PASSWORD)))
                .andExpect(status().isUnauthorized());
    }
}