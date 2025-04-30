package com.allica.allica_bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allica.allica_bank.entity.Retailer;
import com.allica.allica_bank.enums.RetailerType;

@Repository
public interface RetailerRepository extends JpaRepository<Retailer, Long> {

	Optional<Retailer> findByName(String retailerName);
	
    Optional<Retailer> findByRetailType(RetailerType retailType);
	
    Optional<Retailer> findByApiKeyAndRetailType(String apiKey, RetailerType retailType);
}