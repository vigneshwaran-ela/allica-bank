package com.allica.allica_bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allica.allica_bank.entity.AdminLogin;

/**
 * Repository interface for AdminLogin entity.
 * Provides basic CRUD operations and custom query methods for admin login data.
 */
@Repository
public interface AdminLoginRepository extends JpaRepository<AdminLogin, Long> {

    /**
     * Find AdminLogin by username.
     *
     * @param userName the username of the admin
     * @return an Optional containing the AdminLogin if found, otherwise empty
     */
    Optional<AdminLogin> findByUserName(String userName);
}