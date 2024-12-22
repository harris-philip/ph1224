package com.rental.rental_app.repository;

import com.rental.rental_app.entity.VariableHoliday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VariableHolidayRepository extends JpaRepository<VariableHoliday, UUID> {

    Optional<VariableHoliday> findByHolidayNameAndYear(String holidayName, int year);
    List<VariableHoliday> findByYear(int year);
    Page<VariableHoliday> findAllWithPagination(Pageable pageable);

}

