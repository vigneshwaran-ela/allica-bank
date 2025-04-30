package com.allica.allica_bank.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.parameters.Parameter;

/**
 * Unit tests for {@link OpenApiConfig}, validating OpenAPI custom header behavior.
 */
class OpenApiConfigTest {

    /**
     * Tests that the {@code selectiveHeaderCustomizer} method adds the {@code X-API-KEY}
     * header only to operations under "/api/v1/customer" path.
     */
    @Test
    void selectiveHeaderCustomizer_whenApplied_shouldAddApiKeyOnlyToCustomerPaths() {
        // Arrange
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = new OpenAPI();

        // Path that should receive the header
        Operation customerGetOp = new Operation();
        Operation customerPostOp = new Operation();
        PathItem customerPathItem = new PathItem()
                .get(customerGetOp)
                .post(customerPostOp);

        // Path that should NOT receive the header
        Operation otherGetOp = new Operation();
        Operation otherPostOp = new Operation();
        PathItem otherPathItem = new PathItem()
                .get(otherGetOp)
                .post(otherPostOp);

        Paths paths = new Paths();
        paths.addPathItem("/api/v1/customer", customerPathItem);
        paths.addPathItem("/api/v1/admin", otherPathItem);
        openAPI.setPaths(paths);

        // Act
        config.selectiveHeaderCustomizer().customise(openAPI);

        // Assert - "/api/v1/customer" path should have X-API-KEY
        for (Operation op : List.of(customerGetOp, customerPostOp)) {
            List<Parameter> params = op.getParameters();
            assertNotNull(params, "Parameters should not be null for customer path");
            assertTrue(params.stream().anyMatch(p -> "X-API-KEY".equals(p.getName())),
                    "X-API-KEY should be present for customer path");
        }

        // Assert - "/api/v1/admin" path should NOT have X-API-KEY
        for (Operation op : List.of(otherGetOp, otherPostOp)) {
            List<Parameter> params = op.getParameters();
            if (params != null) {
                assertTrue(params.stream().noneMatch(p -> "X-API-KEY".equals(p.getName())),
                        "X-API-KEY should NOT be present for non-customer path");
            }
        }
    }
}
