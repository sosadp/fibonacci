package com.exercise.fibonacci;

import com.exercise.fibonacci.controllers.CalculateFibonacciController;
import com.exercise.fibonacci.dtos.NumberResultDTO;
import com.exercise.fibonacci.services.FibonacciService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculateFibonacciController.class)
public class CalculateFibonacciControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FibonacciService fibonacciService;

    @InjectMocks
    private CalculateFibonacciController calculateFibonacciController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(calculateFibonacciController).build();
    }

    @Test
    void getFibonacci_validNumber_returnsOk() throws Exception {
        // Dados de prueba
        int value = 5;
        NumberResultDTO dto = NumberResultDTO.builder().number(value).fibonacciValue(8L).build();

        when(fibonacciService.calculateFibonacci(value)).thenReturn(Optional.of(dto));

        // Ejecuta el test
        mockMvc.perform(get("/api/v1/fib/{number}", value))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(value))
                .andExpect(jsonPath("$.fibonacciValue").value(8L));
    }


    @Test
    void getFibonacci_callsServiceWithCorrectParameter() throws Exception {
        // Dados de prueba
        int value = 5;
        NumberResultDTO dto = NumberResultDTO.builder().number(value).fibonacciValue(8L).build();

        when(fibonacciService.calculateFibonacci(value)).thenReturn(Optional.of(dto));

        // Ejecuta el test
        mockMvc.perform(get("/api/v1/fib/{number}", value));

        // Verifica que el servicio fue llamado con el parámetro correcto
        verify(fibonacciService).calculateFibonacci(value);
    }
}
