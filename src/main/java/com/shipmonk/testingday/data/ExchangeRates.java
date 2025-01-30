package com.shipmonk.testingday.data;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Ales Krestan
 */
@Entity
public class ExchangeRates {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private boolean success;
    private long timestamp;
    private String base;
    private LocalDate date;
    //        private Map<String, Double> rates;

}
