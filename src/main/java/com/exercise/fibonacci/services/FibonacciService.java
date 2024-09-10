package com.exercise.fibonacci.services;

import com.exercise.fibonacci.dtos.NumberResultDTO;

import java.util.Optional;

public interface FibonacciService {

    Optional<NumberResultDTO> calculateFibonacci(int value);
}
