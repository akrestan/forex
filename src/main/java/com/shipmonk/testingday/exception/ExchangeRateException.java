package com.shipmonk.testingday.exception;

import lombok.Getter;

@Getter
public class ExchangeRateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExchangeRateException(String message) {
        super(message);
    }

    public ExchangeRateException(String message, Throwable cause) {
        super(message, cause);
    }

}
