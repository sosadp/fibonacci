package com.exercise.fibonacci;

import com.exercise.fibonacci.dtos.response.FibonacciStatisticResponse;
import com.exercise.fibonacci.exceptions.FailedDateConvertException;
import com.exercise.fibonacci.models.FibonacciStatistic;
import com.exercise.fibonacci.repositories.FibonacciStatisticRepository;
import com.exercise.fibonacci.services.impl.FibonacciStatisticServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FibonacciStatisticTest {

    @Mock
    private FibonacciStatisticRepository fibonacciStatisticRepository;

    @InjectMocks
    private FibonacciStatisticServiceImpl fibonacciStatisticService;

    @Test
    @DisplayName("Test that validates obtaining a statistics from a given number")
    public void getStatisticByNumberTest(){

        //Given
        int number = 5;
        FibonacciStatistic statistic = FibonacciStatistic.builder().number(number).requestCount(3).build();

        //When
        when(fibonacciStatisticRepository.findByNumber(number)).thenReturn(Optional.ofNullable(statistic));

        Optional<FibonacciStatisticResponse> statisticByNumber = this.fibonacciStatisticService.getStatisticByNumber(number);

        //Then
        assertTrue(statisticByNumber.isPresent());
        assertEquals(5, statisticByNumber.get().number());
        verify(fibonacciStatisticRepository, times(1)).findByNumber(number);
    }

    @Test
    @DisplayName("Test that validates obtaining a list of statistics from a given date")
    public void getStatisticByDate(){
        //Given
        String originDate = "2024-10-15";
        LocalDate dateParsed = LocalDate.parse(originDate);
        List<FibonacciStatistic> statisticListForDateSaved = List.of(
                FibonacciStatistic.builder().number(2).requestCount(3).build(),
                FibonacciStatistic.builder().number(4).requestCount(22).build());

        //When
        when(fibonacciStatisticRepository.findByDateQueryGreaterThanEqual(dateParsed)).thenReturn(Optional.of(statisticListForDateSaved));

        Optional<List<FibonacciStatisticResponse>> statisticByDate = this.fibonacciStatisticService.getStatisticByDate(originDate);

        //Then
        assertTrue(statisticByDate.isPresent());
        verify(fibonacciStatisticRepository, times(1)).findByDateQueryGreaterThanEqual(dateParsed);

    }

    @Test
    @DisplayName("Test to validate that the FailedDateConvertException is launched ")
    public void throwFailedDateConvertExceptionWhenInvalidDateTest(){

        //Given
        String invalidDate ="a";

        //When
        FailedDateConvertException exception = assertThrows(FailedDateConvertException.class, () -> fibonacciStatisticService.getStatisticByDate(invalidDate));

        assertEquals("Failed date convert", exception.getMessage());
    }

}
