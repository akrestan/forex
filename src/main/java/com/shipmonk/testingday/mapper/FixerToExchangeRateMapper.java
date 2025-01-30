package com.shipmonk.testingday.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.shipmonk.testingday.dtos.ExchangeRatesDto;
import com.shipmonk.testingday.dtos.FixerResponse;

/**
 * @author Ales Krestan
 */
@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FixerToExchangeRateMapper {

    ExchangeRatesDto toDto(FixerResponse fixerResponse);

}
