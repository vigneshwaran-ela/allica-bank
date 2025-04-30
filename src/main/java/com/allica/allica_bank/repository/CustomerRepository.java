package com.allica.allica_bank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allica.allica_bank.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByRetailerIdAndLoginName(Long retailerId, String loginName);
    
    Optional<Customer> findByRetailerIdAndId(Long retailerId, Long id);
    
    List<Customer> findByRetailerId(Long retailerId);
}