package com.exercise.fibonacci.dtos.response;

import lombok.Builder;

@Builder
public record FibonacciStatisticResponse(int number, int requestCount) {
}
