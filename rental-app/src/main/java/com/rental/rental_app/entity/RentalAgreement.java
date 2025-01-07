package com.rental.rental_app.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rental_agreement")
public class RentalAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rental_agreement_id", nullable = false, unique = true)
    private UUID rentalAgreementId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tool_id", nullable = false)
    private Tool tool;

    @Column(name = "rental_days", nullable = false)
    private int rentalDays;

    @Column(name = "checkout_date", nullable = false)
    private LocalDate checkoutDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "daily_rental_charge", precision = 10, scale = 2, nullable = false)
    private BigDecimal dailyRentalCharge;

    @Column(name = "chargeable_days", nullable = false)
    private int chargeableDays;

    @Column(name = "discount_percent", precision = 5, scale = 2, nullable = false)
    private BigDecimal discountPercent;

    @Column(name = "pre_discount_charge", precision = 10, scale = 2, nullable = false)
    private BigDecimal preDiscountCharge;

    @Column(name = "discount_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal discountAmount;

    @Column(name = "final_charge", precision = 10, scale = 2, nullable = false)
    private BigDecimal finalCharge;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    public UUID getRentalAgreementId() {
        return rentalAgreementId;
    }

    public void setRentalAgreementId(UUID rentalAgreementId) {
        this.rentalAgreementId = rentalAgreementId;
    }

    public Tool getTool() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getDailyRentalCharge() {
        return dailyRentalCharge;
    }

    public void setDailyRentalCharge(BigDecimal dailyRentalCharge) {
        this.dailyRentalCharge = dailyRentalCharge;
    }

    public int getChargeableDays() {
        return chargeableDays;
    }

    public void setChargeableDays(int chargeableDays) {
        this.chargeableDays = chargeableDays;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public void setPreDiscountCharge(BigDecimal preDiscountCharge) {
        this.preDiscountCharge = preDiscountCharge;
    }

    public BigDecimal getFinalCharge() {
        return finalCharge;
    }

    public void setFinalCharge(BigDecimal finalCharge) {
        this.finalCharge = finalCharge;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}

