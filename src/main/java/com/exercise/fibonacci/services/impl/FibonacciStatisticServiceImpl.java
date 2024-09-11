package com.exercise.fibonacci.services.impl;

import com.exercise.fibonacci.dtos.response.FibonacciStatisticResponse;
import com.exercise.fibonacci.exceptions.CalculateFibonacciException;
import com.exercise.fibonacci.exceptions.FailedDateConvertException;
import com.exercise.fibonacci.models.FibonacciStatistic;
import com.exercise.fibonacci.repositories.FibonacciStatisticRepository;
import com.exercise.fibonacci.services.FibonacciStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<List<FibonacciStatisticResponse>> getStatisticByDate(String date) {

        LocalDate dateForQuery = FormatterValid(date);

        List<FibonacciStatistic> statisticListForDateSaved  = fibonacciStatisticRepository.findByDateQueryGreaterThanEqual(dateForQuery).orElseThrow();

        return Optional.of(statisticListForDateSaved.stream()
                .map(statistic -> FibonacciStatisticResponse.builder().number(statistic.getNumber()).requestCount(statistic.getRequestCount()).build())
                .collect(Collectors.toList()));
    }

    private static LocalDate FormatterValid(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return  LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e){

            throw  new FailedDateConvertException("Failed date convert", new Throwable("Date formatter is invalid, the correct format is yyyy-MM-dd"));
        }
    }
}
