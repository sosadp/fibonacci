package com.exercise.fibonacci.dtos;

import lombok.Builder;

@Builder
public record NumberResultDTO(int number, long fibonacciValue) {
}
