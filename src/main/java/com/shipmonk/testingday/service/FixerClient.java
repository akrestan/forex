package com.shipmonk.testingday.service;

import java.time.Instant;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shipmonk.testingday.dtos.FixerResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ales Krestan
 */
@Slf4j
@Service
public class FixerClient {

    //    private final WebClient webClient;
    //
    //    public FixerClient(WebClient webClient ) {
    //        this.webClient = webClient;
    //    }
    //
    //    public FixerResponse getLatestRates() {
    //        return webClient.get()
    //            .uri(uriBuilder -> uriBuilder
    //                .path("/latest")
    //                .queryParam("access_key", "{access_key}")
    //                .build())
    //            .retrieve()
    //            .onStatus(HttpStatus::isError, response ->
    //                response.bodyToMono(String.class)
    //                    .flatMap(error -> Mono.error(new RuntimeException("API Error: " + error)))
    //            )
    //            .bodyToMono(FixerResponse.class)
    //            .block();
    //    }

    public FixerResponse getOneRate() {
        FixerResponse fixerResponse = FixerResponse.builder()
            .base("EUR")
            .success(true)
            .timestamp(Instant.now().toEpochMilli())
            .rates(Map.of())
            .build();
        return fixerResponse;
    }

}

