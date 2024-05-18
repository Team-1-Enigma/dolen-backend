package com.enigma.dolen.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class AplicationException extends Exception {
    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
