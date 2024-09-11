package com.exercise.fibonacci.controllers;

import com.exercise.fibonacci.dtos.response.FibonacciStatisticResponse;
import com.exercise.fibonacci.services.FibonacciStatisticService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Statistic")
public class FibonacciStatisticController {

    @Autowired
    private FibonacciStatisticService fibonacciStatisticService;

    @GetMapping("/statistic/{number}")
    public ResponseEntity<?> getStatiscticByNumber(@PathVariable("number") int number){

        FibonacciStatisticResponse statisticByNumber = fibonacciStatisticService.getStatisticByNumber(number).orElseThrow();

        return ResponseEntity.ok(statisticByNumber);
    }
    @GetMapping("by-date/{date}")
    public ResponseEntity<?> getStatisticByDate(@PathVariable("date") String date){

        Optional<List<FibonacciStatisticResponse>> statisticByDate = fibonacciStatisticService.getStatisticByDate(date);

        return ResponseEntity.ok(statisticByDate);

    }
}
