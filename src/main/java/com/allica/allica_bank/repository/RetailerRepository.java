package com.allica.allica_bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allica.allica_bank.entity.Retailer;
import com.allica.allica_bank.enums.RetailerType;

/**
 * Repository interface for accessing Retailer data from the database.
 * Provides methods for finding retailers by various attributes such as name, type, and API key.
 */
@Repository
public interface RetailerRepository extends JpaRepository<Retailer, Long> {

    /**
     * Find a retailer by its name.
     *
     * @param retailerName the name of the retailer
     * @return an Optional containing the Retailer if found
     */
	Optional<Retailer> findByName(String retailerName);
	
    /**
     * Find a retailer by its enum-based type.
     *
     * @param retailType the type of the retailer (enum)
     * @return an Optional containing the Retailer if found
     */
    Optional<Retailer> findByType(RetailerType retailType);
	
    /**
     * Find a retailer by its API key and type.
     *
     * @param apiKey     the API key associated with the retailer
     * @param retailType the type of the retailer
     * @return an Optional containing the Retailer if found
     */
    Optional<Retailer> findByApiKeyAndType(String apiKey, RetailerType retailType);
}