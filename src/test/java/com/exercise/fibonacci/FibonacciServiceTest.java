package com.exercise.fibonacci;

import com.exercise.fibonacci.dtos.NumberResultDTO;
import com.exercise.fibonacci.exceptions.CalculateFibonacciException;
import com.exercise.fibonacci.models.Fibonacci;
import com.exercise.fibonacci.repositories.FibonacciRepository;
import com.exercise.fibonacci.repositories.FibonacciStatisticRepository;
import com.exercise.fibonacci.services.impl.FibonacciServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FibonacciServiceTest {

    @Mock
    private FibonacciRepository fibonacciRepository; // Mock del repositorio

    @Mock
    private FibonacciStatisticRepository fibonacciStatisticRepository; // Mock del repositorio de estadísticas

    @InjectMocks
    private FibonacciServiceImpl fibonacciServiceImp; // Inyecta los mocks en la implementación del servicio


    @Test
    @DisplayName("Fibonacci calculation service test returns fibonacci number")
    public void calculateFibonacci(){

        //given
        Fibonacci result = Fibonacci.builder()
                .number(10)
                .fibonacciValue(89)
                .build();

        //when


        Mockito.when(fibonacciRepository.findByNumber(10))
                .thenReturn(Optional.of(result));

        // Llamamos al servicio inyectado con mocks
        Optional<NumberResultDTO> fibonacciResult = fibonacciServiceImp.calculateFibonacci(10);

        //then
        Assertions.assertEquals(89L, fibonacciResult.get().fibonacciValue());
        Mockito.verify(fibonacciRepository, Mockito.times(1)).findByNumber(Mockito.eq(10));

    }

    @Test
    @DisplayName("Test to validate Exception when the parameter is negative")
    public void returnExceptionWhenParameterIsNegativeTest(){
        //given
        int number = -2;

        //when
        Mockito.when(fibonacciServiceImp.calculateFibonacci(number))
                .thenThrow(new CalculateFibonacciException("Invalid fibonacciValue for calculate",
                        new Throwable("the fibonacciValue cannot be negative")));

        // Llamamos al servicio inyectado con mocks
        CalculateFibonacciException calculateFibonacciException = Assertions.assertThrows(
                CalculateFibonacciException.class,
                () -> fibonacciServiceImp.calculateFibonacci(number)
        );

        //then
        Assertions.assertTrue(calculateFibonacciException.getMessage().contains("Invalid fibonacciValue for calculate"));

    }
}
