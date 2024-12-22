package com.rental.rental_app.factories;

import com.rental.rental_app.entity.RentalAgreement;
import com.rental.rental_app.entity.Tool;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import org.springframework.lang.NonNull;

public class RentalAgreementFactory {

    public static RentalAgreement createRentalAgreement(
            @NonNull Tool tool,
            int amountOfDaysRented,
            @NonNull LocalDateTime checkoutDate,
            @NonNull BigDecimal dailyRentalCharge,
            int chargeableDays,
            @NonNull BigDecimal discountPercent
    ) {
        Objects.requireNonNull(tool, "Tool cannot be null");
        Objects.requireNonNull(checkoutDate, "Checkout date cannot be null");
        Objects.requireNonNull(dailyRentalCharge, "Daily rental charge cannot be null");
        Objects.requireNonNull(discountPercent, "Discount percent cannot be null");

        LocalDateTime dueDate = checkoutDate.plusDays(amountOfDaysRented);

        BigDecimal discountAmount = dailyRentalCharge
                .multiply(BigDecimal.valueOf(chargeableDays))
                .multiply(discountPercent.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));

        BigDecimal finalCharge = dailyRentalCharge
                .multiply(BigDecimal.valueOf(chargeableDays))
                .subtract(discountAmount);

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
        rentalAgreement.setFinalCharge(finalCharge);
        rentalAgreement.setCreatedDate(LocalDateTime.now());
        rentalAgreement.setModifiedDate(LocalDateTime.now());

        return rentalAgreement;
    }
}

