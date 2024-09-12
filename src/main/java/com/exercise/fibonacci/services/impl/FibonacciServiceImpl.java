package com.exercise.fibonacci.services.impl;

import com.exercise.fibonacci.dtos.NumberResultDTO;
import com.exercise.fibonacci.exceptions.CalculateFibonacciException;
import com.exercise.fibonacci.models.Fibonacci;
import com.exercise.fibonacci.models.FibonacciStatistic;
import com.exercise.fibonacci.repositories.FibonacciRepository;
import com.exercise.fibonacci.repositories.FibonacciStatisticRepository;
import com.exercise.fibonacci.services.FibonacciService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Servicio para el calculo fibonacci
 */
@Service
@Slf4j
@Transactional
public class FibonacciServiceImpl implements FibonacciService {

    private final FibonacciRepository fibonacciRepository;
    private final FibonacciStatisticRepository fibonacciStatisticRepository;

    //private long[] secuenciMemo;

    private Map<Integer, Long> fibonacciTable = new HashMap<>();

    @Autowired
    public FibonacciServiceImpl(FibonacciRepository fibonacciRepository, FibonacciStatisticRepository fibonacciStatisticRepository) {
        this.fibonacciRepository = fibonacciRepository;
        this.fibonacciStatisticRepository = fibonacciStatisticRepository;
    }

    /**
     * Metodo para el orquestacion del calculo y cacheado de los calculos de fibonacci
     * @param value es el numero que se le calculara la secuencia fibonacci.
     * @return
     */
    @Override
    public Optional<NumberResultDTO> calculateFibonacci(int value) {

        if (value<0){
            throw  new CalculateFibonacciException("Invalid fibonacciValue for calculate",
                    new Throwable("the fibonacciValue cannot to be negative"));
        }

        if (value<=1) {
            return Optional.ofNullable(NumberResultDTO.builder()
                    .number(value)
                    .fibonacciValue(value)
                    .build());
        }
        //protege los recursos del equipo para que no procese serie mayor a 1000.
        if (value>1000){
            throw new CalculateFibonacciException("Invalid fibonacciValue for calculate",
                    new Throwable("The fibonacciValue of n is too large. Try a fibonacciValue less than 1000."));
        }

        // implementacion del cache desde la base de datos
        Optional<Fibonacci> cacheResult = fibonacciRepository.findByNumber(value);

        if (cacheResult.isPresent()){
            // registra la estadistica de consulta.
            statisticRegister(value);

            return Optional.ofNullable(NumberResultDTO.builder()
                    .number(cacheResult.get().getNumber())
                    .fibonacciValue(cacheResult.get().getFibonacciValue())
                    .build());
        }

        //Invoca el calculo de los valores intermedios y transforma en un lista para poder ser persistido.
        fibonacciTable.clear();
        fibonacciTable = fibonacciDp(value);


        List<Fibonacci> resultForSave = fibonacciTable.entrySet().stream()
                .map(fibValue -> Fibonacci.builder().number(fibValue.getKey()).fibonacciValue(fibValue.getValue()).build())
                .toList();

        Long resultFibonacci = fibonacciTable.get(value);

        saveSequenceFibonacci(resultForSave);

        return Optional.ofNullable(NumberResultDTO.builder()
                .number(value)
                .fibonacciValue(resultFibonacci)
                .build());
    }

    private void statisticRegister(int value) {
        FibonacciStatistic fibonacciStatistic = fibonacciStatisticRepository.findByNumber(value)
                .orElseGet(()-> FibonacciStatistic.builder()
                        .number(value)
                        .requestCount(0)
                        .build()
                );
        fibonacciStatistic.setRequestCount(fibonacciStatistic.getRequestCount()+1);
        fibonacciStatisticRepository.save(fibonacciStatistic);
    }

    private Map<Integer, Long> fibonacciDp(int n){

        if (n <= 0) {
            throw new CalculateFibonacciException("Invalid value", new Throwable("The value must not be less than or equal to 0"));
        }
        // Arreglo para almacenar la secuencia de Fibonacci
        long[] fibonacci = new long[n + 1];
        // Valores base
        fibonacci[0] = 0;
        fibonacciTable.put(0, fibonacci[0]);

        if (n > 0) {
            fibonacci[1] = 1;
            fibonacciTable.put(1, fibonacci[1]);
        }
        // Construir la secuencia de Fibonacci utilizando programación dinámica
        for (int i = 2; i <= n; i++) {
            fibonacci[i] = fibonacci[i - 1] + fibonacci[i - 2];
            fibonacciTable.put(i, fibonacci[i]);
        }

        return fibonacciTable;
    }

    private void saveSequenceFibonacci(List<Fibonacci> fibonaccis){
        //Obten los number los intermedios

        List<Integer> numbers = fibonaccis.stream()
                .map(Fibonacci::getNumber)
                .filter(Objects::nonNull)
                .toList();

        //busca los numeros ya persistidos
        List<Fibonacci> existingNumbers = fibonacciRepository.findAllByNumberIn(numbers);

        //conjunto sin numero repetidos
        Set<Integer> existingNumber = existingNumbers.stream()
                .map(Fibonacci::getNumber)
                .collect(Collectors.toSet());

        List<Fibonacci> newFibonacci = fibonaccis.stream()
                .filter( fib -> !existingNumber.contains(fib.getNumber())).toList();

        fibonacciRepository.saveAll(newFibonacci);
    }
}
