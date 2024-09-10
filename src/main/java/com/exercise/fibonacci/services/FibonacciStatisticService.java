package com.exercise.fibonacci.services;

import com.exercise.fibonacci.models.FibonacciStatistic;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FibonacciStatisticService {

    Optional<FibonacciStatistic> getStatisticByNumber(int number);

    List<FibonacciStatistic> getStatisticByDate(Date date);

    List<FibonacciStatistic> getStatisticBetweenDate(Date init, Date end);
}
