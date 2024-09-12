package com.exercise.fibonacci;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;

@TestConfiguration
public class TestAsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        return Runnable::run; // Ejecuta las tareas de forma sincrónica para los tests
    }
}
