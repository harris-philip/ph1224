package com.rental.rental_app.services;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;


public class HolidayService {

    public static boolean isLaborDay(LocalDate date) {
        return date.equals(getFirstMondayInSept(date.getYear()));
    }

    private static LocalDate getFirstMondayInSept(int year) {
        LocalDate firstMondayInSept = LocalDate.of(year, Month.SEPTEMBER, 1);
        return firstMondayInSept.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    public static boolean isIndependenceDay(LocalDate date) {
        return date.equals(getIndependenceDay(date.getYear()));
    }

    private static LocalDate getIndependenceDay(int year) {
        LocalDate independenceDay = LocalDate.of(year, Month.JULY, 4);
        if (isWeekend(independenceDay)) {
            if (independenceDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                independenceDay = independenceDay.plusDays(1);
            } else if (independenceDay.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                independenceDay = independenceDay.minusDays(1);
            }
        }
        return independenceDay;
    }

    public static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }
}
