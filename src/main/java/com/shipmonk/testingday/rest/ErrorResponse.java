package com.shipmonk.testingday.rest;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Data;

/**
 * @author Ales Krestan
 */
@Data
@Builder
public class ErrorResponse {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final OffsetDateTime timestamp;

    private final String message;

    private final String details;

    private final String path;

}
