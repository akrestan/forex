package com.shipmonk.testingday;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.shipmonk.testingday.config.ExchangeRateApplicationTestConfiguration;
import com.shipmonk.testingday.dtos.ExchangeRatesDto;
import com.shipmonk.testingday.exception.AccessDeniedException;
import com.shipmonk.testingday.exception.BadRequestException;
import com.shipmonk.testingday.exception.NotFoundException;
import com.shipmonk.testingday.exception.ServiceUnavailableException;
import com.shipmonk.testingday.service.ExchangeRateService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Import(ExchangeRateApplicationTestConfiguration.class)
@Slf4j
class TestingdayExchangeRatesApplicationTests {

    private @Inject WebTestClient webTestClient;
    private @SpyBean ExchangeRateService exchangeRateService;

    @BeforeEach
    public void setUp() {
        // in seconds
//        waitForHttpResponse(30);
    }

    @Test
    @Disabled
    void contextLoads() throws InterruptedException {
        new CountDownLatch(1).wait();
    }

    @Test
    void fixerCall() {
        String day = "2022-12-03";
        ExchangeRatesDto responseBody = callAndGetResponse(day)
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .expectStatus()
            .isOk()
            .expectBody(ExchangeRatesDto.class)
            .returnResult()
            .getResponseBody();

        assertThat(responseBody.getDate()).isEqualTo(LocalDate.parse(day));
    }

    @Test
    void exceptionHandling() {
        String day = "2022-12-03";
        reset(exchangeRateService);
        when(exchangeRateService.getOne(day)).thenThrow(new BadRequestException("Custom bad request"));
        callAndGetResponse(day)
            .expectStatus()
            .isBadRequest();

        reset(exchangeRateService);
        day = "abcd";
        callAndGetResponse(day)
            .expectStatus()
            .isBadRequest();

        day = "2024-12-12";
        reset(exchangeRateService);
        when(exchangeRateService.getOne(day)).thenThrow(new NotFoundException("Data for " + day + " not found"));
        callAndGetResponse(day)
            .expectStatus()
            .isNotFound();

        reset(exchangeRateService);
        when(exchangeRateService.getOne(day)).thenThrow(new ServiceUnavailableException("Service currently unavailable"));
        callAndGetResponse(day)
            .expectStatus()
            .is5xxServerError();

        reset(exchangeRateService);
        when(exchangeRateService.getOne(day)).thenThrow(new AccessDeniedException("Unauthorized request to the provider"));
        callAndGetResponse(day)
            .expectStatus()
            .isUnauthorized();

    }

    private ResponseSpec callAndGetResponse(String day) {
        return webTestClient
            .get()
            .uri(uriBuilder ->
                uriBuilder.path("/api/v1/rates/{day}")
                    .build(day)
            )
            .exchange();
    }

    protected void waitForHttpResponse(long seconds) {
        webTestClient = webTestClient.mutate()
            .responseTimeout(Duration.ofSeconds(seconds))
            .build();
    }

}
