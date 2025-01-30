package com.shipmonk.testingday.dtos;

import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ales Krestan
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRatesDto {

    private boolean success;
    private long timestamp;
    private String base;
    private LocalDate date;
    private Map<String, Double> rates;

}
