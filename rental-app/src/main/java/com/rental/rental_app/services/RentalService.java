package com.rental.rental_app.services;

import com.rental.rental_app.entity.RentalAgreement;
import com.rental.rental_app.repository.RentalAgreementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RentalService {

    private final RentalAgreementRepository rentalAgreementRepository;

    public RentalService(RentalAgreementRepository rentalAgreementRepository) {
        this.rentalAgreementRepository = rentalAgreementRepository;
    }

    public RentalAgreement save(RentalAgreement agreement) {
        return rentalAgreementRepository.save(agreement);
    }

    public Page<RentalAgreement> findAllRentalAgreements(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("checkoutDate").descending());
        return rentalAgreementRepository.findAllWithPagination(pageable);
    }

    public void deleteRentalAgreement(UUID agreementId) {
        rentalAgreementRepository.deleteById(agreementId);
    }

}
