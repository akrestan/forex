package com.shipmonk.testingday.rest;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public static final String DEFAULT_BASE_CURRENCY = "EUR";

    private final ExchangeRateService exchangeRate;

    @GetMapping(path = "/{day}")
    public ResponseEntity<ExchangeRatesDto> getDayForBaseRates(@Valid @Pattern(regexp = dayPattern) @PathVariable("day") String day,
                                                               @RequestParam("baseCurrency") Optional<String> baseCurrencyOpt) {
        return ResponseEntity.ok()
            .body(exchangeRate.getForDayAndBase(day, baseCurrencyOpt.orElse(DEFAULT_BASE_CURRENCY)));
    }

    @GetMapping("/latest")
    public ResponseEntity<ExchangeRatesDto> getLatestRates(@RequestParam("baseCurrency") Optional<String> baseCurrencyOpt) {
        return ResponseEntity.ok()
            .body(exchangeRate.getLatest(baseCurrencyOpt.orElse(DEFAULT_BASE_CURRENCY)));
    }

}
