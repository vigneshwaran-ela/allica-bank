package com.allica.allica_bank.enums;

import java.util.Arrays;

/**
 * Enum representing supported retailer types.
 */
public enum RetailerType {

	AMAZON("Amazon"),
	FLIPKART("Flipkart"),
	WALMART("Walmart"),
	ZEPTO("Zepto");
	
	private String name;
	
	RetailerType(String name) {
		this.name = name;
	}

	 /**
     * Returns the display name of the retailer.
     * @return retailer name as String
     */
	public String getName() {
		return name;
	}
	
    /**
     * Maps a display name to the corresponding RetailerType enum.
     *
     * @param name the name to look up
     * @return matching RetailerType
     * @throws IllegalArgumentException if name does not match any retailer
     */
	public static RetailerType fromName(String name) {
	    return Arrays.stream(RetailerType.values())
	            .filter(type -> type.getName().equalsIgnoreCase(name))
	            .findFirst()
	            .orElseThrow(() -> new IllegalArgumentException("Invalid retailer name: " + name));
	}
	
}
