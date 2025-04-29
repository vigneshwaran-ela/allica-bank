package com.allica.allica_bank.model.retailer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * DTO for responding with retailer information.
 * Fields with null values will be excluded from the JSON output.
 */
@JsonInclude(value = Include.NON_NULL)
public class RetailerResponseDTO {

    private Long id;
    
    private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
