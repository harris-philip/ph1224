package com.rental.rental_app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "variable_holiday")
public class VariableHoliday extends Holiday {

    @Column(nullable = false)
    private int year;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}