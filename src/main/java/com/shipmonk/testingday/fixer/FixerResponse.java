package com.shipmonk.testingday.fixer;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FixerResponse {

    private boolean success;
    private long timestamp;
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;

    @JsonCreator
    public FixerResponse(
        @JsonProperty(value = "success", required = true) boolean success,
        @JsonProperty(value = "timestamp", required = true) long timestamp,
        @JsonProperty(value = "base", required = true) String base,
        @JsonProperty(value = "date", required = true) String date,
        @JsonProperty(value = "rates", required = true) Map<String, BigDecimal> rates) {
        this.success = success;
        this.timestamp = timestamp;
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

}
