package com.exercise.fibonacci.services.impl;

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
    public Optional<FibonacciStatistic> getStatisticByNumber(int number) {
        return Optional.empty();
    }

    @Override
    public List<FibonacciStatistic> getStatisticByDate(Date date) {
        return List.of();
    }

    @Override
    public List<FibonacciStatistic> getStatisticBetweenDate(Date init, Date end) {
        return List.of();
    }
}
