package com.shipmonk.testingday.fixer;

/**
 * @author Ales Krestan
 */
public interface FixerClient {

    FixerResponse getLatest(String baseCurrency);

    FixerResponse getByDayAndBaseCurrency(String day, String baseCurrency);

}
