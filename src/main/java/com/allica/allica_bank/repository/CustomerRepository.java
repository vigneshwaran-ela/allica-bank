package com.allica.allica_bank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allica.allica_bank.entity.Customer;

/**
 * Repository interface for accessing Customer entity data.
 * Provides methods to fetch customers by retailer and ID combinations.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Find a customer by retailer ID and login name.
     *
     * @param retailerId the ID of the retailer
     * @param loginName  the login name of the customer
     * @return an Optional containing the Customer if found
     */
    Optional<Customer> findByRetailerIdAndLoginName(Long retailerId, String loginName);
    
    /**
     * Find a customer by both retailer ID and customer ID.
     *
     * @param retailerId the ID of the retailer
     * @param id         the ID of the customer
     * @return an Optional containing the Customer if found
     */
    Optional<Customer> findByRetailerIdAndId(Long retailerId, Long id);
    
    /**
     * Retrieve all customers for a given retailer.
     *
     * @param retailerId the ID of the retailer
     * @return list of customers belonging to the specified retailer
     */
    List<Customer> findByRetailerId(Long retailerId);
}