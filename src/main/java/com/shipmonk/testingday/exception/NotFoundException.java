package com.shipmonk.testingday.exception;

public class NotFoundException extends ExchangeRateException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
