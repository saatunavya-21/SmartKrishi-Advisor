package com.smartkrishi.advisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartkrishi.advisor.entity.Farmer;

import java.util.Optional;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    Optional<Farmer> findByEmail(String email);
}