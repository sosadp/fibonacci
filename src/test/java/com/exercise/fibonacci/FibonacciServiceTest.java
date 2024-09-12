package com.exercise.fibonacci;

import com.exercise.fibonacci.dtos.NumberResultDTO;
import com.exercise.fibonacci.exceptions.CalculateFibonacciException;
import com.exercise.fibonacci.models.Fibonacci;
import com.exercise.fibonacci.models.FibonacciStatistic;
import com.exercise.fibonacci.repositories.FibonacciRepository;
import com.exercise.fibonacci.repositories.FibonacciStatisticRepository;
import com.exercise.fibonacci.services.impl.FibonacciServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import(TestAsyncConfig.class)
public class FibonacciServiceTest {

    @Mock
    private FibonacciRepository fibonacciRepository;

    @Mock
    private FibonacciStatisticRepository fibonacciStatisticRepository;

    @Mock
    private Executor taskExecutor;

    @InjectMocks
    private FibonacciServiceImpl fibonacciService;

    @Test
    @DisplayName("Test to validate the Exception when the value is negative")
    void calculateFibonacci_negativeValue_throwsException() {
        int invalidValue = -1;

        CalculateFibonacciException exception = assertThrows(CalculateFibonacciException.class, () ->
                fibonacciService.calculateFibonacci(invalidValue));

        assertEquals("Invalid fibonacciValue for calculate", exception.getMessage());
    }

    @Test
    @DisplayName("Test to validate the return when the value is 1")
    public void calculateFibonacciValueLessThanOrEqualTo1ReturnsValue() {
        int value = 1;

        Optional<NumberResultDTO> result = fibonacciService.calculateFibonacci(value);

        assertTrue(result.isPresent());
        assertEquals(value, result.get().number());
        assertEquals(value, result.get().fibonacciValue());
    }

    @Test
    @DisplayName("Test to validate the limit up to 1001")
    public void calculateFibonacciValueGreaterThan1000ThrowsExceptionTest() {
        int invalidValue = 1001;

        CalculateFibonacciException exception = assertThrows(CalculateFibonacciException.class, () ->
                fibonacciService.calculateFibonacci(invalidValue));

        assertEquals("Invalid fibonacciValue for calculate", exception.getMessage());
    }

    @Test
    @DisplayName("Test to validate the use of the cache")
    public void calculateFibonacciValueInCacheReturnsCachedResult() {
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
    @DisplayName("Test to validate the increase in statistics")
    public void statisticRegisterIncrementsRequestCountTest() {

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
