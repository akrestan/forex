package com.shipmonk.testingday.fixer;

import java.io.IOException;

import javax.xml.bind.DataBindingException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipmonk.testingday.exception.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author Ales Krestan
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FixerClientImpl implements FixerClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Override
    public FixerResponse getLatest() {
        //        WebClient clonedWebClient = webClient.mutate().defaultHeader(HttpHeaders.CONTENT_TYPE, jsonCharUtf8.toString()).build();
        WebClient clonedWebClient = webClient.mutate().build();
        byte[] responseBytes = clonedWebClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/latest")
                .queryParam("access_key", "{access_key}")
                .build())
            //            .accept(parseMediaType("application/json;charset=UTF-8"), parseMediaType("application/json; Charset=UTF-8"))
            //            .acceptCharset(StandardCharsets.UTF_8)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                HttpStatus status = clientResponse.statusCode();
                switch (status) {
                    case BAD_REQUEST -> throw new BadRequestException("Fixer bad request");
                    case UNAUTHORIZED -> throw new AccessDeniedException("Fixer unauthorized");
                    default ->
                        throw new ExchangeRateException(String.format("Uncategorized client error HTTP response code %d from Fixer", status));
                }
            })
            .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                HttpStatus status = clientResponse.statusCode();
                switch (status) {
                    case SERVICE_UNAVAILABLE -> throw new ServiceUnavailableException("Fixer service unavailable");
                    default ->
                        throw new ExchangeRateException(String.format("Uncategorized HTTP error response code %d from Fixer", status));
                }

            })
            .bodyToMono(byte[].class)
            .onErrorResume(Exception.class, e -> {
                String msg = "Caught " + e.getClass().getSimpleName() + " while calling Fixer exchange rates provider";
                log.error(msg, e);
                return Mono.error(new ServiceUnavailableException(msg, e));
            })
            .block();

        try {
            return objectMapper.readValue(responseBytes, FixerResponse.class);
        } catch (IOException | DataBindingException ex1) {
            try {
                FixerError fixerError = objectMapper.readValue(responseBytes, FixerError.class);
                if (!fixerError.isSuccess()) {
                    if (fixerError.getError().code == 101) {
                        throw new AccessDeniedException("Fixer access error: " + fixerError.getError());
                    } else {
                        throw new FixerClientException("Error calling Fixer " + fixerError.getError());
                    }
                }
                throw new ExchangeRateException("Unknown Fixer error", ex1);
            } catch (IOException | DataBindingException ex2) {
                throw new ExchangeRateException("Error reading Fixer response", ex2);
            }
        }
    }

}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class FixerError {

    public boolean success;

    @JsonProperty("error")
    public Error error;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class Error {

        public int code;
        public String type;
        public String info;

    }

}


