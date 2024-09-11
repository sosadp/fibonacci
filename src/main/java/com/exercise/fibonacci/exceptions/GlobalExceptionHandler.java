package com.exercise.fibonacci.exceptions;

import com.exercise.fibonacci.dtos.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * Clase para manejar el formato de la respuesta de errores que se muestra al usuario del API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({CalculateFibonacciException.class, MethodArgumentTypeMismatchException.class, FailedDateConvertException.class})
    public ResponseEntity<?> handlerCalculateFibonacciController(Exception e){
        ExceptionResponse response = ExceptionResponse.builder()
                .timestamp( new Date().toString())
                .status(400)
                .message(e.getMessage())
                .error(e.getCause().getMessage())
                .build();

        log.error("Exception error ", e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
