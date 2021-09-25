package com.truesoft.miriad.coreservice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.truesoft.miriad.apicore.api.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(ConstraintViolationException e) {
        log.warn("Validation failed, errorMessage={}", e.getMessage());

        final ErrorResponse response = ErrorResponse.builder()
            .code("validation.failed")
            .message(Arrays.asList(e.getMessage()))
            .build();

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("Validation failed, errorMessage={}", e.getMessage());

        final List<String> errors = e.getBindingResult().getFieldErrors().stream()
            .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());

        final ErrorResponse response = ErrorResponse.builder()
            .code("validation.failed")
            .message(errors)
            .build();

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("Method not supported, errorMessage={}", e.getMessage());

        final ErrorResponse response = ErrorResponse.builder()
            .code("bad.request")
            .message(Arrays.asList("method.not.supported"))
            .build();

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        log.error("Unknown error", e);

        final ErrorResponse response = ErrorResponse.builder()
            .code("server.error")
            .build();

        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
