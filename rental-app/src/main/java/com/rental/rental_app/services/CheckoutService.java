package com.rental.rental_app.services;

import com.rental.rental_app.entity.RentalAgreement;
import com.rental.rental_app.entity.Tool;
import com.rental.rental_app.entity.ToolRentalInfo;
import com.rental.rental_app.exceptions.InvalidDiscountRateException;
import com.rental.rental_app.exceptions.InvalidRentalDaysException;
import com.rental.rental_app.factories.RentalAgreementFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Service
public class CheckoutService {

    private final RentalService rentalService;
    private final ToolService toolService;

    public CheckoutService(RentalService rentalService, ToolService toolService) {
        this.rentalService = rentalService;
        this.toolService = toolService;
    }

    public void checkout(String toolCode, int rentalDayCount, int discountPercent, LocalDate checkoutDate) throws InvalidRentalDaysException, InvalidDiscountRateException {
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
        logRentalAgreement(rentalAgreement);
    }

    private void logRentalAgreement(RentalAgreement rentalAgreement) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("----- Rental Agreement -----\n");
        addLine(stringBuilder, "Tool code: ", rentalAgreement.getTool().getToolCode());
        addLine(stringBuilder, "Tool type: ", rentalAgreement.getTool().getToolType().getToolTypeName());
        addLine(stringBuilder, "Tool brand: ", rentalAgreement.getTool().getBrand());
        addLine(stringBuilder, "Rental days: ", rentalAgreement.getRentalDays());
        addLine(stringBuilder, "Check out date: ", dateTimeFormatter.format(rentalAgreement.getCheckoutDate()));
        addLine(stringBuilder, "Due date: ", dateTimeFormatter.format(rentalAgreement.getDueDate()));
        addLine(stringBuilder, "Daily rental charge: ", formatCurrencyAmounts(rentalAgreement.getDailyRentalCharge()));
        addLine(stringBuilder, "Charge days: ", rentalAgreement.getChargeableDays());
        addLine(stringBuilder, "Pre-discount charge: ", formatCurrencyAmounts(rentalAgreement.getPreDiscountCharge()));
        addLine(stringBuilder, "Discount Percent: ", rentalAgreement.getDiscountPercent() + "%");
        addLine(stringBuilder, "Discount Amount: ", formatCurrencyAmounts(rentalAgreement.getDiscountAmount()));
        addLine(stringBuilder, "Final charge: ", formatCurrencyAmounts(rentalAgreement.getFinalCharge()));
        System.out.println(stringBuilder);
    }

    private void addLine(StringBuilder stringBuilder, String prefix, Object value) {
        stringBuilder.append(prefix).append(value).append("\n");
    }

    private String formatCurrencyAmounts(BigDecimal value) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        return currencyFormat.format(value);
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
            isHoliday = HolidayService.isIndependenceDay(possibleChargeableDate);
        } else if (possibleChargeableDate.getMonth().equals(Month.SEPTEMBER)) {
            isHoliday = HolidayService.isLaborDay(possibleChargeableDate);
        }
        boolean isWeekend = HolidayService.isWeekend(possibleChargeableDate);

        if (isHoliday && !rentalInfo.isHasHolidayCharge()) {
            return false;
        } else if (!isWeekend && !rentalInfo.isHasWeekdayCharge()) {
            return false;
        } else return !isWeekend || rentalInfo.isHasWeekendCharge();
    }

    private BigDecimal calculateDiscountAmount(BigDecimal preDiscountCharge, BigDecimal discountPercent) {
        return preDiscountCharge.multiply(
                    discountPercent.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
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
