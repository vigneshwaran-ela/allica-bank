package com.allica.allica_bank.entity;

import com.allica.allica_bank.enums.RetailerType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity representing a retailer in the system.
 * Maps to the `retailer` table in the database.
 */
@Entity
@Table(name = "retailer")
public class Retailer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, unique = true)
	private RetailerType type;

	@Column(name = "api_key", nullable = false, unique = true)
	private String apiKey;

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

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public RetailerType getType() {
		return this.type;
	}

	public void setType(RetailerType type) {
		this.type = type;
	}

}
