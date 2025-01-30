package com.shipmonk.testingday.rest.advice;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.shipmonk.testingday.exception.AccessDeniedException;
import com.shipmonk.testingday.exception.BadRequestException;
import com.shipmonk.testingday.exception.NotFoundException;
import com.shipmonk.testingday.exception.ServiceUnavailableException;
import com.shipmonk.testingday.rest.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    // our or Fixer's
    @ExceptionHandler({ ValidationException.class, BindException.class, BadRequestException.class })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception e, HttpServletRequest request) {
        return respondWith(HttpStatus.BAD_REQUEST, e.getMessage(), causeMessageOrEmpty(e), request);
    }

    // our or Fixer's
    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<ErrorResponse> handleNotFound(Exception e, HttpServletRequest request) {
        return respondWith(HttpStatus.NOT_FOUND, e.getMessage(), causeMessageOrEmpty(e), request);
    }

    // our
    @ExceptionHandler({ ServiceUnavailableException.class })
    public ResponseEntity<ErrorResponse> handleServiceUnavailable(Exception e, HttpServletRequest request) {
        return respondWith(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage(), causeMessageOrEmpty(e), request);
    }

    // Fixer's
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ErrorResponse> handleAccessDenied(Exception e, HttpServletRequest request) {
        return respondWith(HttpStatus.UNAUTHORIZED, e.getMessage(), causeMessageOrEmpty(e), request);
    }

    protected ResponseEntity<ErrorResponse> respondWith(HttpStatus status, String message, String details, HttpServletRequest request) {
        return ResponseEntity.status(status).body(
            ErrorResponse.builder()
                .timestamp(OffsetDateTime.now(ZoneId.of("UTC")))
                .message(message)
                .details(details)
                .path(request.getRequestURI())
                .build());
    }

    protected ResponseEntity<String> respondWith(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(message);
    }

    private static String causeMessageOrEmpty(Exception e) {
        return Optional.ofNullable(e.getCause()).map(Throwable::getMessage).orElse("");
    }

}
