package com.exercise.fibonacci.exceptions;

public class FailedDateConvertException extends RuntimeException{

    private int statusCode;

    public FailedDateConvertException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public FailedDateConvertException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 400;
    }
}
