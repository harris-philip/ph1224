package com.rental.rental_app.repository;

import com.rental.rental_app.entity.ToolRentalInfo;
import com.rental.rental_app.entity.ToolType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ToolRentalInfoRepository extends JpaRepository<ToolRentalInfo, UUID> {
    Page<ToolRentalInfo> findAllWithPagination(Pageable pageable);
    Optional<ToolRentalInfo> findByToolType(ToolType toolType);
}
