package com.shipmonk.testingday.exception;

/**
 * @author Ales Krestan
 */
public class FixerClientException extends ExchangeRateException {

    public FixerClientException(String message) {
        super(message);
    }

    public FixerClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
