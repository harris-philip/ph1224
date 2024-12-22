package com.rental.rental_app.entity;

import java.util.UUID;

public interface ToolProjection {
    UUID getToolId();
    String getToolCode();
    String getBrand();
    String getToolTypeName();
    double getDailyCharge();
    boolean hasWeekdayCharge();
    boolean hasWeekendCharge();
    boolean hasHolidayCharge();
}
