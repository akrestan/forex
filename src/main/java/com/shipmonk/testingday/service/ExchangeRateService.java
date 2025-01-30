package com.shipmonk.testingday.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Service;

import com.shipmonk.testingday.dtos.ExchangeRatesDto;
import com.shipmonk.testingday.dtos.FixerResponse;
import com.shipmonk.testingday.exception.BadRequestException;
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

    public ExchangeRatesDto getOne(String day) {
        FixerResponse oneRate = fixerClient.getOneRate();
        try {
            oneRate.setDate(LocalDate.parse(day));
        } catch (DateTimeParseException dtpe) {
            log.error("Error parsing date {}, reason {}", day, dtpe.getMessage());
            throw new BadRequestException("Error parsing date " + day, dtpe);
        }
        return mapper.toDto(oneRate);
    }

}
