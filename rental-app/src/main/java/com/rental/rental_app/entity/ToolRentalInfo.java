package com.rental.rental_app.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tool_rental_info")
public class ToolRentalInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID toolRentalInfoId;

    @OneToOne(optional = false)
    @JoinColumn(name = "tool_type_id", nullable = false)
    private ToolType toolType;

    @Column(nullable = false, precision = 10, scale = 2)
    private double dailyCharge;

    @Column(nullable = false)
    private boolean hasWeekdayCharge;

    @Column(nullable = false)
    private boolean hasWeekendCharge;

    @Column(nullable = false)
    private boolean hasHolidayCharge;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    // Getters and Setters
    public UUID getToolRentalInfoId() {
        return toolRentalInfoId;
    }

    public void setToolRentalInfoId(UUID toolRentalInfoId) {
        this.toolRentalInfoId = toolRentalInfoId;
    }

    public ToolType getToolType() {
        return toolType;
    }

    public void setToolType(ToolType toolType) {
        this.toolType = toolType;
    }

    public double getDailyCharge() {
        return dailyCharge;
    }

    public void setDailyCharge(double dailyCharge) {
        this.dailyCharge = dailyCharge;
    }

    public boolean isHasWeekdayCharge() {
        return hasWeekdayCharge;
    }

    public void setHasWeekdayCharge(boolean hasWeekdayCharge) {
        this.hasWeekdayCharge = hasWeekdayCharge;
    }

    public boolean isHasWeekendCharge() {
        return hasWeekendCharge;
    }

    public void setHasWeekendCharge(boolean hasWeekendCharge) {
        this.hasWeekendCharge = hasWeekendCharge;
    }

    public boolean isHasHolidayCharge() {
        return hasHolidayCharge;
    }

    public void setHasHolidayCharge(boolean hasHolidayCharge) {
        this.hasHolidayCharge = hasHolidayCharge;
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
