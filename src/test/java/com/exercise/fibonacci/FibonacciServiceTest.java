package com.exercise.fibonacci;

import com.exercise.fibonacci.dtos.NumberResultDTO;
import com.exercise.fibonacci.exceptions.CalculateFibonacciException;
import com.exercise.fibonacci.models.Fibonacci;
import com.exercise.fibonacci.models.FibonacciStatistic;
import com.exercise.fibonacci.repositories.FibonacciRepository;
import com.exercise.fibonacci.repositories.FibonacciStatisticRepository;
import com.exercise.fibonacci.services.impl.FibonacciServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FibonacciServiceTest {

    @Mock
    private FibonacciRepository fibonacciRepository;

    @Mock
    private FibonacciStatisticRepository fibonacciStatisticRepository;

    @InjectMocks
    private FibonacciServiceImpl fibonacciService;

    @Test
    void calculateFibonacci_negativeValue_throwsException() {
        int invalidValue = -1;

        CalculateFibonacciException exception = assertThrows(CalculateFibonacciException.class, () ->
                fibonacciService.calculateFibonacci(invalidValue));

        assertEquals("Invalid fibonacciValue for calculate", exception.getMessage());
    }

    @Test
    void calculateFibonacci_valueLessThanOrEqualTo1_returnsValue() {
        int value = 1;

        Optional<NumberResultDTO> result = fibonacciService.calculateFibonacci(value);

        assertTrue(result.isPresent());
        assertEquals(value, result.get().number());
        assertEquals(value, result.get().fibonacciValue());
    }

    @Test
    void calculateFibonacci_valueGreaterThan1000_throwsException() {
        int invalidValue = 1001;

        CalculateFibonacciException exception = assertThrows(CalculateFibonacciException.class, () ->
                fibonacciService.calculateFibonacci(invalidValue));

        assertEquals("Invalid fibonacciValue for calculate", exception.getMessage());
    }

    @Test
    void calculateFibonacci_valueInCache_returnsCachedResult() {
        int value = 10;
        Fibonacci cachedFibonacci = Fibonacci.builder()
                .number(value)
                .fibonacciValue(55L)
                .build();

        when(fibonacciRepository.findByNumber(value)).thenReturn(Optional.of(cachedFibonacci));

        Optional<NumberResultDTO> result = fibonacciService.calculateFibonacci(value);

        assertTrue(result.isPresent());
        assertEquals(value, result.get().number());
        assertEquals(55L, result.get().fibonacciValue());

        verify(fibonacciStatisticRepository, times(1)).findByNumber(value);
        verify(fibonacciRepository, times(1)).findByNumber(value);
    }

    @Test
    void calculateFibonacci_valueNotInCache_calculatesAndSaves() {

        //Given
        // Datos de prueba
        int value = 5;
        Long expectedValue = 8L;


        //When
        // Llamada al método
        when(fibonacciRepository.findByNumber(value)).thenReturn(Optional.empty());

        Optional<NumberResultDTO> result = fibonacciService.calculateFibonacci(value);

        //Then
        // Verificar el resultado
        assertTrue(result.isPresent());

        // Verificar que el resultado de Fibonacci fue guardado
        ArgumentCaptor<Fibonacci> fibonacciArgumentCaptor = ArgumentCaptor.forClass(Fibonacci.class);
        verify(fibonacciRepository).save(fibonacciArgumentCaptor.capture());
        assertEquals(5, fibonacciArgumentCaptor.getValue().getNumber());


    }

    @Test
    void statisticRegister_incrementsRequestCount() {

        //given
        int value = 10;
        FibonacciStatistic existingStatistic = FibonacciStatistic.builder()
                .number(value)
                .requestCount(5)
                .build();

        Fibonacci cacheResult = Fibonacci.builder()
                .number(value)
                .fibonacciValue(56)
                .build();
        //When
        when(fibonacciRepository.findByNumber(value)).thenReturn(Optional.of(cacheResult));

        when(fibonacciStatisticRepository.findByNumber(value)).thenReturn(Optional.of(existingStatistic));

        Optional<NumberResultDTO> numberResultDTO = fibonacciService.calculateFibonacci(value);

        //then
        assertTrue(numberResultDTO.isPresent());
        assertEquals(6, existingStatistic.getRequestCount());
        verify(fibonacciStatisticRepository, times(1)).save(any(FibonacciStatistic.class));


    }
}
