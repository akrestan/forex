package com.shipmonk.testingday.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Ales Krestan
 */
@Entity
public class ExchangeRates {

    @Id
    private Long id;
    private boolean success;
    private long timestamp;
    private String base;
    private LocalDate date;
    //            private Map<String, Double> rates;

}
