package com.rental.rental_app.repository;

import com.rental.rental_app.entity.RentalAgreement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RentalAgreementRepository extends JpaRepository<RentalAgreement, UUID> {

    Page<RentalAgreement> findAllWithPagination(Pageable pageable);
}
