package com.allica.allica_bank.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.parameters.HeaderParameter;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenApiCustomizer globalHeaderCustomizer() {
		return openApi -> {
			if (openApi.getPaths() != null) {
				openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {

					operation.addParametersItem(new HeaderParameter().name("X-API-KEY")
							.description("Like password").required(false).example("password"));
				}));
			}
		};
	}

}
