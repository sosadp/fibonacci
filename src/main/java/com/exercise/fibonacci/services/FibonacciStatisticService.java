package com.exercise.fibonacci.services;

import com.exercise.fibonacci.dtos.response.FibonacciStatisticResponse;

import java.util.List;
import java.util.Optional;

public interface FibonacciStatisticService {

    Optional<FibonacciStatisticResponse> getStatisticByNumber(int number);

    Optional<List<FibonacciStatisticResponse>> getStatisticByDate(String date);

}
