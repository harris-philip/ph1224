package com.rental.rental_app.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@MappedSuperclass
public abstract class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID holidayId;

    @Column(nullable = false, unique = true)
    private String holidayName;

    @Column(nullable = false)
    private LocalDate holidayDate;

    @Column(nullable = false)
    private LocalDate createdDate;

    public UUID getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(UUID holidayId) {
        this.holidayId = holidayId;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
