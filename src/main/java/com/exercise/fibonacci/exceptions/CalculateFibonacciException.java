package com.exercise.fibonacci.exceptions;

public class CalculateFibonacciException extends RuntimeException{

    public CalculateFibonacciException(String message) {
        super(message);

    }

    public CalculateFibonacciException(String message, Throwable cause) {
        super(message, cause);

    }

}
