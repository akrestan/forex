package com.shipmonk.testingday.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.Builder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ales Krestan
 */
@Lazy
@TestConfiguration
@Slf4j
public class ExchangeRateApplicationTestConfiguration {

    @Autowired(required = false)
    private NettyReactiveWebServerFactory nettyReactiveWebServerFactory;

    @Bean
    public int serverPort(@LocalServerPort int serverPort) {
        return serverPort;
    }

    @Bean
    public WebTestClient webTestClient(int serverPort) {

        detectHttpContainer(serverPort);
        Builder webClientBuilder = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + serverPort);

        return webClientBuilder
            .build();
    }

    private void detectHttpContainer(int serverPort) {
        if (nettyReactiveWebServerFactory != null) {
            log.info("Netty HTTP container on port: {}", serverPort);
        } else {
            log.info("Servlet-based HTTP container on port: {}", serverPort);
        }
    }

}
