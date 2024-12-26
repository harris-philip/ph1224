package com.rental.rental_app.services;

import com.rental.rental_app.entity.RentalAgreement;
import com.rental.rental_app.entity.Tool;
import com.rental.rental_app.entity.ToolRentalInfo;
import com.rental.rental_app.exceptions.InvalidDiscountRateException;
import com.rental.rental_app.exceptions.InvalidRentalDaysException;
import com.rental.rental_app.factories.RentalAgreementFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class CheckoutService {
    private final static Logger LOGGER = LogManager.getLogger(CheckoutService.class);

    private final RentalService rentalService;
    private final ToolService toolService;
    private final HolidayService holidayService;

    public CheckoutService(RentalService rentalService, ToolService toolService, HolidayService holidayService) {
        this.rentalService = rentalService;
        this.toolService = toolService;
        this.holidayService = holidayService;
    }

    public RentalAgreement checkout(String toolCode, int rentalDayCount, int discountPercent, LocalDate checkoutDate) throws InvalidRentalDaysException, InvalidDiscountRateException {
        if (rentalDayCount < 1) {
            throw new InvalidRentalDaysException("Rental Days entered is less than 1, please enter a number greater than or equal to 1 for rental days");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new InvalidDiscountRateException("Discount Rate is not within the range of 0-100, please enter a number between the range 0-100");
        }
        Tool tool = toolService.findToolByCode(toolCode);
        ToolRentalInfo rentalInfo = toolService.findRentalInfoByToolType(tool.getToolType());
        List<LocalDate> rentalPeriod = calculateRentalPeriod(rentalDayCount, checkoutDate);
        int chargeableDays = calculateChargeableDays(rentalPeriod, rentalInfo);
        BigDecimal preDiscountAmount = calculatePreDiscountAmount(chargeableDays, BigDecimal.valueOf(rentalInfo.getDailyCharge()));
        BigDecimal discountAmount = calculateDiscountAmount(preDiscountAmount, BigDecimal.valueOf(discountPercent));
        RentalAgreement rentalAgreement = RentalAgreementFactory.createRentalAgreement(tool, rentalDayCount, checkoutDate,
                BigDecimal.valueOf(rentalInfo.getDailyCharge()), chargeableDays, rentalPeriod.getLast(),
                BigDecimal.valueOf(discountPercent), preDiscountAmount, discountAmount,
                calculateFinalCharge(preDiscountAmount, discountAmount));
        rentalService.save(rentalAgreement);
        LOGGER.info(rentalAgreement.toString());
        return rentalAgreement;
    }

    private int calculateChargeableDays(List<LocalDate> rentalPeriod, ToolRentalInfo rentalInfo) {
        return (int) rentalPeriod.stream().filter(date -> isChargeable(date, rentalInfo)).count();
    }

    private List<LocalDate> calculateRentalPeriod(int rentalDayCount, LocalDate checkoutDate) {
        LocalDate firstDay = checkoutDate.plusDays(1);
        return IntStream.range(0, rentalDayCount).mapToObj(firstDay::plusDays).toList();
    }

    private boolean isChargeable(LocalDate possibleChargeableDate, ToolRentalInfo rentalInfo) {
        boolean isHoliday = false;
        if (possibleChargeableDate.getMonth().equals(Month.JULY)) {
            isHoliday = holidayService.isIndependenceDay(possibleChargeableDate);
        } else if (possibleChargeableDate.getMonth().equals(Month.SEPTEMBER)) {
            isHoliday = holidayService.isLaborDay(possibleChargeableDate);
        }
        boolean isWeekend = holidayService.isWeekend(possibleChargeableDate);

        if (isHoliday && !rentalInfo.isHasHolidayCharge()) {
            return false;
        } else if (!isWeekend && !rentalInfo.isHasWeekdayCharge()) {
            return false;
        } else return !isWeekend || rentalInfo.isHasWeekendCharge();
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
