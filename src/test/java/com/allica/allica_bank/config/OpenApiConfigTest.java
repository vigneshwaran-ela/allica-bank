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
     * Tests that the {@code globalHeaderCustomizer} method adds the {@code X-API-KEY}
     * header to all HTTP operations in the OpenAPI documentation.
     */
    @Test
    void globalHeaderCustomizer_whenApplied_shouldAddApiKeyHeaderToAllOperations() {
        // Arrange
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = new OpenAPI();

        Operation getOperation = new Operation();
        Operation postOperation = new Operation();

        PathItem pathItem = new PathItem()
                .get(getOperation)
                .post(postOperation);

        Paths paths = new Paths();
        paths.addPathItem("/test", pathItem);
        openAPI.setPaths(paths);

        // Act
        config.globalHeaderCustomizer().customise(openAPI);

        // Assert
        for (Operation operation : List.of(getOperation, postOperation)) {
            List<Parameter> parameters = operation.getParameters();
            assertNotNull(parameters, "Operation parameters should not be null");

            boolean hasApiKey = parameters.stream()
                    .anyMatch(p -> "X-API-KEY".equals(p.getName()));

            assertTrue(hasApiKey, "X-API-KEY header should be present in operation");
        }
    }
}
