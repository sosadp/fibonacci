package com.exercise.fibonacci.dtos.response;

import lombok.Builder;

@Builder
public record ExceptionResponse(String timestamp, int status, String error, String message) {
}
