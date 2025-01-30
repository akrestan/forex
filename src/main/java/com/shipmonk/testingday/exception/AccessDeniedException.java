package com.shipmonk.testingday.exception;

public class AccessDeniedException extends ExchangeRateException {

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

}
