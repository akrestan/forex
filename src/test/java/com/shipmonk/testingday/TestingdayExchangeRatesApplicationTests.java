package com.shipmonk.testingday;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.shipmonk.testingday.dtos.ExchangeRatesDto;
import com.shipmonk.testingday.exception.AccessDeniedException;
import com.shipmonk.testingday.exception.BadRequestException;
import com.shipmonk.testingday.exception.NotFoundException;
import com.shipmonk.testingday.exception.ServiceUnavailableException;
import com.shipmonk.testingday.service.ExchangeRateService;
import com.shipmonk.testingday.spring.ExchangeRateApplicationTestConfiguration;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Import(ExchangeRateApplicationTestConfiguration.class)
@Slf4j
@ActiveProfiles("test")
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
    void getByDay() {
        String aDay = "2022-12-03";
        ExchangeRatesDto responseBody = callAndGetResponse(aDay)
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .expectStatus()
            .isOk()
            .expectBody(ExchangeRatesDto.class)
            .returnResult()
            .getResponseBody();
        assertThat(responseBody.getDate()).isEqualTo(LocalDate.parse(aDay));
    }

    @Test
    void httpErrorAndExceptionHandling() {
        String aDay = "2022-12-03";
        reset(exchangeRateService);
        when(exchangeRateService.getByDay(aDay)).thenThrow(new BadRequestException("Custom bad request"));
        callAndGetResponse(aDay)
            .expectStatus()
            .isBadRequest();

        reset(exchangeRateService);
        aDay = "abcd";
        callAndGetResponse(aDay)
            .expectStatus()
            .isBadRequest();

        aDay = "2024-12-12";
        reset(exchangeRateService);
        when(exchangeRateService.getByDay(aDay)).thenThrow(new NotFoundException("Data for " + aDay + " not found"));
        callAndGetResponse(aDay)
            .expectStatus()
            .isNotFound();

        reset(exchangeRateService);
        when(exchangeRateService.getByDay(aDay)).thenThrow(new ServiceUnavailableException("Service currently unavailable"));
        callAndGetResponse(aDay)
            .expectStatus()
            .is5xxServerError();

        reset(exchangeRateService);
        when(exchangeRateService.getByDay(aDay)).thenThrow(new AccessDeniedException("Unauthorized request to the provider"));
        callAndGetResponse(aDay)
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
