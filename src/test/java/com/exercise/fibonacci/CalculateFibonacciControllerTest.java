package com.exercise.fibonacci;

import com.exercise.fibonacci.controllers.CalculateFibonacciController;
import com.exercise.fibonacci.dtos.NumberResultDTO;
import com.exercise.fibonacci.exceptions.CalculateFibonacciException;
import com.exercise.fibonacci.exceptions.GlobalExceptionHandler;
import com.exercise.fibonacci.services.FibonacciService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {CalculateFibonacciController.class, GlobalExceptionHandler.class})
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
    }

    @Test
    @DisplayName("Test to test the invocation of the controller with correct values return 200")
    public void callControllerValidNumber_returnsOk() throws Exception {
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
    @DisplayName("Test to test the invocation of service with correct values")
    public void callServiceWithCorrectParameter() throws Exception {
        // Dados de prueba
        int value = 5;
        NumberResultDTO dto = NumberResultDTO.builder().number(value).fibonacciValue(8L).build();

        when(fibonacciService.calculateFibonacci(value)).thenReturn(Optional.of(dto));

        // Ejecuta el test
        mockMvc.perform(get("/api/v1/fib/{number}", value));

        // Verifica que el servicio fue llamado con el parámetro correcto
        verify(fibonacciService).calculateFibonacci(value);
    }

    @Test
    @DisplayName("Invalid value test throws CalculateFibonacciException handled exception return 400")
    public void calculateFibonacciExceptionHandlingTest() throws Exception {
        //Given
        int invalid = -5;
        CalculateFibonacciException exception = new CalculateFibonacciException("Invalid value", new Throwable("The value must not be less than or equal to 0"));

        //When and then
        when(fibonacciService.calculateFibonacci(invalid)).thenThrow(exception);
        // Simula el lanzamiento de la excepción CalculateFibonacciException
        mockMvc.perform(get("/api/v1/fib/{number}", invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid value"))
                .andExpect(jsonPath("$.error").value("The value must not be less than or equal to 0"));
    }

}
