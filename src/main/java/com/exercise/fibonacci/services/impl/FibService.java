package com.exercise.fibonacci.services.impl;

import com.exercise.fibonacci.dtos.NumberResultDTO;
import com.exercise.fibonacci.services.FibonacciService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FibService implements FibonacciService {
    List<NumberResultDTO> database = new LinkedList<>();

    @Override
    public Optional<NumberResultDTO> calculateFibonacci(int value) {

        int fibonacci = fibonacci(value);
        return Optional.ofNullable(NumberResultDTO.builder().number(value).fibonacciValue(fibonacci).build());

    }

    public int fibonacci(int n){

        NumberResultDTO partialResult = getFibLowerOrEqualsThan(n);

        if(partialResult != null && partialResult.number()==n){
            return (int) partialResult.fibonacciValue();
        }

        List<NumberResultDTO> intermediateResults = new LinkedList<>();

        var result = fibonacciRec(n, partialResult, intermediateResults);

        savedInDB(intermediateResults);

        return result;

    }

    private void savedInDB(List<NumberResultDTO> memo) {
        memo.forEach(fibResult -> {
            if (database.stream().noneMatch(fibResult1 -> fibResult1.number() == fibResult.number())) {
                database.add(fibResult);
            }
        } );

    }

    private int fibonacciRec(int n, NumberResultDTO partialResult, List<NumberResultDTO> intermediateResults) {
        var result = switch (n){
            case 0,1 -> n;
            default -> {
                if(partialResult == null){
                    yield fibonacciRec(n-1, null, intermediateResults) + fibonacciRec(n - 2, null, intermediateResults);
                } else if (n -1 == partialResult.number()) {
                    yield partialResult.fibonacciValue() + fibonacciRec(n -2, partialResult, intermediateResults);
                } else if (n-2 == partialResult.number()) {
                    yield fibonacciRec(n-1, partialResult, intermediateResults) + partialResult.fibonacciValue();
                } else {
                    yield fibonacciRec(n-1, partialResult, intermediateResults) + fibonacciRec(n-2, partialResult, intermediateResults);
                }
            }
        };

        //if (n>= 2){
            intermediateResults.add( new NumberResultDTO(n, result));
        //}

        return (int) result;
    }

    private NumberResultDTO getFibLowerOrEqualsThan(int value) {

        return database.stream()
                .sorted((a, b) -> b.number() - a.number())
                .filter(fibResult -> fibResult.number()<= value)
                .findAny().orElse(null);
    }

    public static void main(String[] args) {

        FibService fibService = new FibService();
        int value = 300;
        Optional<NumberResultDTO> numberResultDTO = fibService.calculateFibonacci(value);

        System.out.println("number is = " + value + "fib = " + numberResultDTO.get().fibonacciValue() );

        fibService.database.forEach(fib  -> System.out.println("F(n) = " + fib.number() + " fib = " + fib.fibonacciValue() ) );

    }

}
