package com.allica.allica_bank.model.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * DTO for customer response payload.
 * Only non-null fields will be included in the JSON response.
 */
@JsonInclude(value = Include.NON_NULL)
public class CustomerResponseDTO {
	
	private Long id;

	private String firstName;
	
	private String lastName;
	
	private String loginName;
	
	private String dob;
	
	private Long retailerId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Long getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(Long retailerId) {
		this.retailerId = retailerId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
