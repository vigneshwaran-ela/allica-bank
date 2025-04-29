package com.allica.allica_bank.model.retailer;

import com.allica.allica_bank.enums.RetailerType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for retailer creation requests.
 * Contains necessary fields for retailer registration.
 */
public class RetailerRequestDTO {

	@NotNull(message = "Retailer name must not be null")
	@NotBlank(message = "Reatiler name must not be blank")
    private String name;

	@NotNull(message = "Retailer type must not be null")
	private RetailerType type;
	
    @NotNull(message = "ApiKey must not be blank")
    @NotBlank(message = "ApiKey must not be blank")
    private String apiKey;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public RetailerType getRetailType() {
		return type;
	}

	public void setRetailType(RetailerType type) {
		this.type = type;
	}
}
