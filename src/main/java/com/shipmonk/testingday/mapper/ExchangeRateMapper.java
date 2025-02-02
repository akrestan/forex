package com.shipmonk.testingday.mapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jooq.lambda.Seq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.shipmonk.testingday.dtos.ExchangeRatesDto;
import com.shipmonk.testingday.entity.CurrencyRate;
import com.shipmonk.testingday.entity.DayBase;
import com.shipmonk.testingday.fixer.FixerResponse;

/**
 * @author Ales Krestan
 */
@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ExchangeRateMapper {

    ExchangeRatesDto fromFixerResponse(FixerResponse fixerResponse);

    @Mapping(target = "date", source = "day")
    @Mapping(target = "base", source = "baseCode")
    @Mapping(target = "timestamp", source = "obtainedAt")
    @Mapping(target = "rates", source = "currencyRates")
    ExchangeRatesDto fromEntity(DayBase dayBaseEntity);

    default Map<String, BigDecimal> toMap(Set<CurrencyRate> currencyRates) {
        return Seq.seq(currencyRates)
            .collect(Collectors.toMap(CurrencyRate::getCode, currencyRate -> currencyRate.getRate().stripTrailingZeros()));
    }

    default long mapInstantToLong(Instant instant) {
        return instant != null ? instant.toEpochMilli() : 0;
    }
}
