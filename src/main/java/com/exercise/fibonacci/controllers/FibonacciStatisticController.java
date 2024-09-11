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

@RestController
@RequestMapping("/api/v1/statistic")
@Tag(name = "Statistic")
public class FibonacciStatisticController {

    private final FibonacciStatisticService fibonacciStatisticService;

    @Autowired
    public FibonacciStatisticController(FibonacciStatisticService fibonacciStatisticService) {
        this.fibonacciStatisticService = fibonacciStatisticService;
    }

    @GetMapping("/{number}")
    public ResponseEntity<?> getStatiscticByNumber(@PathVariable("number") int number){

        FibonacciStatisticResponse statisticByNumber = fibonacciStatisticService.getStatisticByNumber(number).orElseThrow();

        return ResponseEntity.ok(statisticByNumber);
    }
}
