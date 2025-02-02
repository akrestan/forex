package com.shipmonk.testingday.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.EventObject;
import java.util.Map;
import java.util.Objects;

import org.jooq.lambda.Seq;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.shipmonk.testingday.dtos.ExchangeRatesDto;
import com.shipmonk.testingday.entity.CurrencyRate;
import com.shipmonk.testingday.entity.DayBase;
import com.shipmonk.testingday.fixer.FixerClient;
import com.shipmonk.testingday.fixer.FixerResponse;
import com.shipmonk.testingday.mapper.ExchangeRateMapper;
import com.shipmonk.testingday.repository.DayBaseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ales Krestan
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ApplicationEventPublisher eventBusDispatcher;
    private final DayBaseRepository dayBaseRepository;
    private final ExchangeRateMapper mapper;
    private final FixerClient fixerClient;

    public ExchangeRatesDto getLatest(String baseCurrency) {
        FixerResponse latest = fixerClient.getLatest(baseCurrency);
        var day = latest.getDate();
        var baseCode = latest.getBase();
        DayBase dayBase = dayBaseRepository.findByDayAndBaseCode(day, baseCode)
            .orElse(DayBase.builder()
                .day(day)
                .baseCode(baseCode)
                .obtainedAt(Instant.ofEpochSecond(latest.getTimestamp()))
                .build());
        eventBusDispatcher.publishEvent(new DataEvent(this, dayBase, latest.getRates()));
        return mapper.fromFixerResponse(latest);
    }

    public ExchangeRatesDto getForDayAndBase(String forDay, String baseCurrency) {

        return dayBaseRepository.findByDayAndBaseCode(forDay, baseCurrency)
            .map(mapper::fromEntity)
            .orElseGet(() -> {
                FixerResponse retrieved = fixerClient.getByDayAndBaseCurrency(forDay, baseCurrency);
                var day = retrieved.getDate();
                var baseCode = retrieved.getBase();
                DayBase dayBase = DayBase.builder()
                    .day(day)
                    .baseCode(baseCode)
                    .obtainedAt(Instant.ofEpochSecond(retrieved.getTimestamp()))
                    .build();
                eventBusDispatcher.publishEvent(new DataEvent(this, dayBase, retrieved.getRates()));
                return mapper.fromFixerResponse(retrieved);
            });
    }

    @EventListener
    @Async
    public void handleNewRatesData(DataEvent event) {
        try {
            DayBase dayBase = event.dayBase;
            dayBase.setCurrencyRates(Seq.seq(event.currencyRates.entrySet())
                .map(entry ->
                    CurrencyRate.builder()
                        .dayBase(event.dayBase)
                        .code(entry.getKey())
                        .rate(entry.getValue())
                        .build())
                .toSet());
            dayBaseRepository.save(dayBase);
        } catch (Exception e) {
            log.error("Error saving data from event {}", event, e);
        }
    }

}

class DataEvent extends EventObject {

    final DayBase dayBase;
    final Map<String, BigDecimal> currencyRates;

    public DataEvent(Object source, DayBase dayBase, Map<String, BigDecimal> currencyRates) {
        super(source);
        Objects.requireNonNull(dayBase);
        Objects.requireNonNull(currencyRates);
        this.dayBase = dayBase;
        this.currencyRates = currencyRates;
    }

}

