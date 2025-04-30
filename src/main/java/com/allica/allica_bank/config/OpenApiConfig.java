package com.allica.allica_bank.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.parameters.HeaderParameter;

/**
 * Configuration class to customize the OpenAPI documentation.
 * 
 * Adds a global header parameter "X-API-KEY" to all API operations. This helps
 * document that the header is expected for authorization purposes, even if it's
 * not enforced by Spring Security in this layer.
 */
@Configuration
public class OpenApiConfig {

	/**
	 * Adds a global header parameter ("X-API-KEY") to every operation in the
	 * OpenAPI documentation.
	 *
	 * @return OpenApiCustomizer that modifies the OpenAPI spec
	 */
	@Bean
	public OpenApiCustomizer selectiveHeaderCustomizer() {
		return openApi -> {
			if (openApi.getPaths() != null) {
				openApi.getPaths().forEach((path, pathItem) -> {
					// Only apply to paths that start with /api/v1/customer
					if (path.startsWith("/api/v1/customer")) {
						pathItem.readOperations().forEach(operation -> {
							operation.addParametersItem(new HeaderParameter().name("X-API-KEY")
									.description("Plain text apiKey").required(false).example("password"));
						});
					}
				});
			}
		};

	}

}
