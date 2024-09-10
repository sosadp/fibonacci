package com.exercise.fibonacci.controllers;

import com.exercise.fibonacci.dtos.NumberResultDTO;
import com.exercise.fibonacci.services.FibonacciService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CalculateFibonacciController {

    private final FibonacciService fibonacciService;

    public CalculateFibonacciController(FibonacciService fibonacciService) {
        this.fibonacciService = fibonacciService;
    }

    @GetMapping("/fib/{number}")
    public ResponseEntity<?> getFibonacci(@PathVariable("number") int number){
        NumberResultDTO numberResultDTO = fibonacciService.calculateFibonacci(number).orElseThrow();

        return ResponseEntity.ok(numberResultDTO);
    }
}
