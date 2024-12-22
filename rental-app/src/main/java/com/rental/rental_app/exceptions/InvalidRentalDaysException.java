package com.rental.rental_app.exceptions;

public class InvalidRentalDaysException extends Exception {

    public InvalidRentalDaysException(String message) {
        super(message);
    }

    public InvalidRentalDaysException(String message, Throwable cause) {
        super(message, cause);
    }
}
