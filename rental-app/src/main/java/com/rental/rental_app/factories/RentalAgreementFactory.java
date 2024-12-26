package com.rental.rental_app.factories;

import com.rental.rental_app.entity.RentalAgreement;
import com.rental.rental_app.entity.Tool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.springframework.lang.NonNull;

public class RentalAgreementFactory {

    public static RentalAgreement createRentalAgreement(Tool tool, int amountOfDaysRented,
        LocalDate checkoutDate, BigDecimal dailyRentalCharge, int chargeableDays, LocalDate dueDate, BigDecimal discountPercent,
        BigDecimal preDiscountCharge, BigDecimal discountAmount, BigDecimal finalCharge) {

        RentalAgreement rentalAgreement = new RentalAgreement();
        rentalAgreement.setRentalAgreementId(UUID.randomUUID());
        rentalAgreement.setTool(tool);
        rentalAgreement.setRentalDays(amountOfDaysRented);
        rentalAgreement.setCheckoutDate(checkoutDate);
        rentalAgreement.setDueDate(dueDate);
        rentalAgreement.setDailyRentalCharge(dailyRentalCharge);
        rentalAgreement.setChargeableDays(chargeableDays);
        rentalAgreement.setDiscountPercent(discountPercent);
        rentalAgreement.setDiscountAmount(discountAmount);
        rentalAgreement.setPreDiscountCharge(preDiscountCharge);
        rentalAgreement.setFinalCharge(finalCharge);
        rentalAgreement.setCreatedDate(LocalDateTime.now());
        rentalAgreement.setModifiedDate(LocalDateTime.now());

        return rentalAgreement;
    }
}

