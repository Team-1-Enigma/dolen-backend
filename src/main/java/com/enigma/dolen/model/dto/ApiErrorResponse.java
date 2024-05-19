package com.enigma.dolen.model.dto;

public record ApiErrorResponse(Integer statusCode, String statusName, String message, String path, String method) {
}
