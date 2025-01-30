package com.shipmonk.testingday.rest;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shipmonk.testingday.dtos.ExchangeRatesDto;
import com.shipmonk.testingday.service.ExchangeRateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(
    path = "/api/v1/rates",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
@Validated
public class ExchangeRatesController {

    //TODO could be improved
    private static final String dayPattern = "^(20\\d{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";

    private final ExchangeRateService exchangeRate;

    @GetMapping(path = "/{day}")
    public ResponseEntity<ExchangeRatesDto> getRates(@Valid @Pattern(regexp = dayPattern) @PathVariable("day") String day) {
        return ResponseEntity.ok()
            .body(exchangeRate.getOne(day));
    }

}
