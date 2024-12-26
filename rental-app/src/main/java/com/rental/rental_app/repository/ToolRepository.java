package com.rental.rental_app.repository;

import com.rental.rental_app.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ToolRepository extends JpaRepository<Tool, UUID> {
    Optional<Tool> findByToolCode(String toolCode);
}
