package com.shipmonk.testingday.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import reactor.netty.http.client.HttpClient;

/**
 * @author Ales Krestan
 */
@Configuration
public class WebClientConfig {

    @Value("${fixer.api.key}")
    private String apiKey;

    //    @Bean
    //    public WebClient webClient() {
    //        return WebClient.builder()
    //            .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
    //            .baseUrl("")
    //            .defaultUriVariables(Collections.singletonMap("access_key", apiKey))
    //            .build();
    //    }

}


