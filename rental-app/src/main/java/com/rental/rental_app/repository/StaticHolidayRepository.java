package com.rental.rental_app.repository;

import com.rental.rental_app.entity.StaticHoliday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StaticHolidayRepository extends JpaRepository<StaticHoliday, UUID> {

    Page<StaticHoliday> findAllWithPagination(Pageable pageable);

}

