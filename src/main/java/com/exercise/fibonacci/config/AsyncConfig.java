package com.exercise.fibonacci.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // Configura el pool de hilos
        executor.setCorePoolSize(10); // Número mínimo de hilos activos
        executor.setMaxPoolSize(50);  // Número máximo de hilos permitidos
        executor.setQueueCapacity(100); // Capacidad de la cola de tareas
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }
}
