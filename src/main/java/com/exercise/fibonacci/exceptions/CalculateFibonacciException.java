package com.exercise.fibonacci.exceptions;

public class CalculateFibonacciException extends RuntimeException{

    int statusCode;

    public CalculateFibonacciException(String message) {
        super(message);
        statusCode = 400;
    }

    public CalculateFibonacciException(String message, Throwable cause) {
        super(message, cause);
        statusCode = 400;
    }


}
