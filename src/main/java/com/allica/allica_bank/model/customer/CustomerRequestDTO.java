package com.allica.allica_bank.model.customer;

import com.allica.allica_bank.annotation.UniqueLoginName;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO for customer creation and update operations.
 */
public class CustomerRequestDTO {

	@NotNull(message = "First name must not be null")
	@NotBlank(message = "First name must not be blank")
	private String firstName;
	
	private String lastName;
	
	@NotNull(message = "DOB must not be null")
	@NotBlank(message = "DOB must not be blank")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "DOB must be in format yyyy-MM-dd")
	private String dob;
	
	@UniqueLoginName
	@NotNull(message = "Login name must not be null")
	@NotBlank(message = "Login name must not be blank")
	private String loginName;

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

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
