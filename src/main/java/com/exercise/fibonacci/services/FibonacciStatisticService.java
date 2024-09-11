package com.exercise.fibonacci.services;

import com.exercise.fibonacci.dtos.response.FibonacciStatisticResponse;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FibonacciStatisticService {

    Optional<FibonacciStatisticResponse> getStatisticByNumber(int number);

    List<FibonacciStatisticResponse> getStatisticByDate(Date date);

    List<FibonacciStatisticResponse> getStatisticBetweenDate(Date init, Date end);
}
