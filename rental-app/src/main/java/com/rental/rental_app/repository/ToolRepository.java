package com.rental.rental_app.repository;

import com.rental.rental_app.entity.Tool;
import com.rental.rental_app.entity.ToolProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ToolRepository extends JpaRepository<Tool, UUID> {
    @Query("SELECT t.toolId AS toolId, t.toolCode AS toolCode, t.brand AS brand, tt.toolTypeName AS toolTypeName, " +
    "tri.getDailyCharge AS getDailyCharge, tri.hasWeekdayCharge AS hasWeekdayCharge, tri.hasWeekendCharge AS hasWeekendCharge, " +
    "tri.hasHolidayCharge AS hasHolidayCharge FROM Tool t JOIN t.toolType tt JOIN tt.toolRentalInfo tri")
    Page<ToolProjection> findAllToolProjections(Pageable pageable);
}
