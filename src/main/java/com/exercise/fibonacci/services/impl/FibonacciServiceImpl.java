package com.exercise.fibonacci.services.impl;

import com.exercise.fibonacci.dtos.NumberResultDTO;
import com.exercise.fibonacci.exceptions.CalculateFibonacciException;
import com.exercise.fibonacci.models.Fibonacci;
import com.exercise.fibonacci.models.FibonacciStatistic;
import com.exercise.fibonacci.repositories.FibonacciRepository;
import com.exercise.fibonacci.repositories.FibonacciStatisticRepository;
import com.exercise.fibonacci.services.FibonacciService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * Servicio para el calculo fibonacci
 */
@Service
@Slf4j
public class FibonacciServiceImpl implements FibonacciService {

    private final FibonacciRepository fibonacciRepository;
    private final FibonacciStatisticRepository fibonacciStatisticRepository;

    private long[] secuenciMemo;

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

        //  obtiene el resultado fibonacci para ser almacenado en la tabla cache y retornado al usuario.
        BigInteger resultFibonacci = computeFibonacci(value);

        secuenciMemo = new long[value+1];
        secuenciMemo = fibonacciDp(value);
        for (long num: secuenciMemo){
            log.info("Fibonacci ==={}",num );
        }

        Fibonacci fibonacci = Fibonacci.builder()
                .number(value)
                .fibonacciValue(resultFibonacci.longValue())
                .build();

        fibonacciRepository.save(fibonacci);

        return Optional.ofNullable(NumberResultDTO.builder()
                .number(value)
                .fibonacciValue(resultFibonacci.longValue())
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

    private BigInteger computeFibonacci(int n) {
        // Usamos Stream.iterate en lugar de IntStream para manejar el array
        return Stream.iterate(new BigInteger[]{BigInteger.ONE, BigInteger.TWO},
                        fib -> new BigInteger[]{fib[1], fib[0].add(fib[1])})
                .limit(n)
                .map(fib -> fib[0])
                .reduce((first, second) -> second)
                .orElseThrow();
    }

    private long[] fibonacciDp(int n){

        if (n <= 0) {
            return new long[]{};
        }

        // Arreglo para almacenar la secuencia de Fibonacci
        long[] fibonacci = new long[n + 1];

        // Valores base
        fibonacci[0] = 0;

        if (n > 0) {
            fibonacci[1] = 1;
        }

        // Construir la secuencia de Fibonacci utilizando programación dinámica
        for (int i = 2; i <= n; i++) {
            fibonacci[i] = fibonacci[i - 1] + fibonacci[i - 2];
        }

        return fibonacci;
    }
}
