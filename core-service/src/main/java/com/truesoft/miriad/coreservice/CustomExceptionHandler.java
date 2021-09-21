package com.truesoft.miriad.coreservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.truesoft.miriad.apicore.api.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        log.error("Unknown error", e);

        final ErrorResponse response = ErrorResponse.builder()
            .code("server.error")
            .build();

        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
