package com.shipmonk.testingday.spring;

import java.util.Collections;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

/**
 * @author Ales Krestan
 */
@Setter
@Validated
@Configuration
@ConfigurationProperties("fixer")
@Slf4j
public class WebClientConfig {

    private @NotBlank String apiKey;
    private @NotBlank String url;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .filter(requestLogger)
            .filter(responseLogger)
            .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
            .baseUrl(url)
            .defaultUriVariables(Collections.singletonMap("access_key", apiKey))
            .build();
    }

    static ExchangeFilterFunction requestLogger = org.springframework.web.reactive.function.client.ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
        log.info("Fixer request: {} {}", clientRequest.method(), clientRequest.url());
        clientRequest.headers()
            .forEach((name, values) -> values.forEach(value -> log.info("--- {}={}", name, value)));
        return Mono.just(clientRequest);
    });

    static ExchangeFilterFunction responseLogger = ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
        log.info("Fixer response: {}", clientResponse.statusCode());
        clientResponse.headers().asHttpHeaders()
            .forEach((name, values) -> values.forEach(value -> log.info("--- {}={}", name, value)));
        return Mono.just(clientResponse);
    });

}
