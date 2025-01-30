package com.shipmonk.testingday.dtos;

import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class FixerResponse {

    private boolean success;
    private long timestamp;
    private String base;
    private LocalDate date;
    private Map<String, Double> rates;

}
