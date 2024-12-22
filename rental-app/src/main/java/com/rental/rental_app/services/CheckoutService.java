package com.rental.rental_app.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class CheckoutService {

    private final RentalService rentalService;

    public CheckoutService(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    private LocalDate calculateDueDate(LocalDate checkoutDate, int rentalDays) {
        return checkoutDate.plusDays(rentalDays);
    }

    private BigDecimal calculateDiscountAmount(BigDecimal preDiscountCharge, BigDecimal discountPercent) {
        return preDiscountCharge.multiply(
                    discountPercent.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                ).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePreDiscountAmount(int chargeableDays, BigDecimal dailyRentalCharge) {
        return dailyRentalCharge.multiply(BigDecimal.valueOf(chargeableDays))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateFinalCharge(BigDecimal preDiscountCharge, BigDecimal discountAmount) {
        return preDiscountCharge.subtract(discountAmount);
    }
}
