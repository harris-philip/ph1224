package com.rental.rental_app.exceptions;

public class InvalidDiscountRateException extends Exception {

    public InvalidDiscountRateException(String message) {
        super(message);
    }

    public InvalidDiscountRateException(String message, Throwable cause) {
        super(message, cause);
    }
}
