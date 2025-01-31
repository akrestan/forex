package com.shipmonk.testingday.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shipmonk.testingday.dtos.ExchangeRatesDto;
import com.shipmonk.testingday.exception.BadRequestException;
import com.shipmonk.testingday.fixer.FixerClient;
import com.shipmonk.testingday.fixer.FixerResponse;
import com.shipmonk.testingday.mapper.FixerToExchangeRateMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ales Krestan
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateService {

    private final FixerToExchangeRateMapper mapper;
    private final FixerClient fixerClient;

    public ExchangeRatesDto getLatest() {
        return mapper.toDto(fixerClient.getLatest());
    }

    public ExchangeRatesDto getByDay(String day) {
        FixerResponse aFixerResponse = dummyFixerResponse();
        try {
            aFixerResponse.setDate(day);
        } catch (DateTimeParseException dtpe) {
            log.error("Error parsing date {}, reason {}", day, dtpe.getMessage());
            throw new BadRequestException("Error parsing date " + day, dtpe);
        }
        return mapper.toDto(aFixerResponse);
    }

    /**
     * dummy
     */
    public ExchangeRatesDto getDummyFixerResponse() {
        return mapper.toDto(dummyFixerResponse());
    }

    private FixerResponse dummyFixerResponse() {

        long timestamp = Instant.now().toEpochMilli();
        Date date = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return FixerResponse.builder()
            .base("EUR")
            .success(true)
            .timestamp(timestamp)
            .date(formatter.format(date))
            .rates(Map.of("CAD", BigDecimal.valueOf(1.260046),
                "CHF", BigDecimal.valueOf(0.933058),
                "USD", BigDecimal.valueOf(1.23461),
                "GBP", BigDecimal.valueOf(0.919154)))
            .build();
    }

}
