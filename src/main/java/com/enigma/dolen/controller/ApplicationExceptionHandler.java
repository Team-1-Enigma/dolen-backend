package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.ApiErrorResponse;
import com.enigma.dolen.model.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplicationException(ApplicationException e, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                e.getErrorCode(),
                e.getMessage(),
                e.getHttpStatus().value(),
                e.getHttpStatus().name(),
                request.getRequestURI(),
                request.getMethod()
        );

        return new ResponseEntity<>(response, e.getHttpStatus());
    }
}
