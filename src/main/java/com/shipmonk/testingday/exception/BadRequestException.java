package com.shipmonk.testingday.exception;

public class BadRequestException extends ExchangeRateException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
