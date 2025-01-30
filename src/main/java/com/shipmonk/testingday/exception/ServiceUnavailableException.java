package com.shipmonk.testingday.exception;

public class ServiceUnavailableException extends ExchangeRateException {

    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

}
