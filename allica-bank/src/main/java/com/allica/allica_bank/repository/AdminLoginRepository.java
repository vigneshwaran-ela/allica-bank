package com.allica.allica_bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allica.allica_bank.entity.AdminLogin;

@Repository
public interface AdminLoginRepository extends JpaRepository<AdminLogin, Long> {

    Optional<AdminLogin> findByUserName(String userName);
}