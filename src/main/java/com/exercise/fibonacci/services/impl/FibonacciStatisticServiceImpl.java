package com.exercise.fibonacci.services.impl;

import com.exercise.fibonacci.dtos.response.FibonacciStatisticResponse;
import com.exercise.fibonacci.exceptions.CalculateFibonacciException;
import com.exercise.fibonacci.models.FibonacciStatistic;
import com.exercise.fibonacci.repositories.FibonacciStatisticRepository;
import com.exercise.fibonacci.services.FibonacciStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FibonacciStatisticServiceImpl implements FibonacciStatisticService {

    private final FibonacciStatisticRepository fibonacciStatisticRepository;

    @Autowired
    public FibonacciStatisticServiceImpl(FibonacciStatisticRepository fibonacciStatisticRepository) {
        this.fibonacciStatisticRepository = fibonacciStatisticRepository;
    }

    @Override
    public Optional<FibonacciStatisticResponse> getStatisticByNumber(int number) {

        FibonacciStatistic statisticByNumber = fibonacciStatisticRepository
                .findByNumber(number)
                .orElseThrow(()->new CalculateFibonacciException("No such element", new Throwable("No value present")));

        return Optional.ofNullable(FibonacciStatisticResponse.builder()
                .number(statisticByNumber.getNumber())
                .requestCount(statisticByNumber.getRequestCount())
                .build());

    }

    @Override
    public List<FibonacciStatisticResponse> getStatisticByDate(Date date) {
        return List.of();
    }

    @Override
    public List<FibonacciStatisticResponse> getStatisticBetweenDate(Date init, Date end) {
        return List.of();
    }
}
