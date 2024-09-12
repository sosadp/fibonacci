package com.exercise.fibonacci;

import com.exercise.fibonacci.controllers.FibonacciStatisticController;
import com.exercise.fibonacci.dtos.response.FibonacciStatisticResponse;
import com.exercise.fibonacci.services.FibonacciStatisticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

@WebMvcTest(FibonacciStatisticControllerTest.class)
public class FibonacciStatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FibonacciStatisticService fibonacciStatisticService;

    @InjectMocks
    private FibonacciStatisticController fibonacciStatisticController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(fibonacciStatisticController).build();
    }


    @Test
    @DisplayName("Test to test the invocation of the controller with correct values")
    public void getStatisticReturnOK() throws Exception {
        int value = 5;
        FibonacciStatisticResponse dto = FibonacciStatisticResponse.builder().number(value).requestCount(8).build();

        when(fibonacciStatisticService.getStatisticByNumber(value)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/v1/statistic/{number}",value))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(value))
                .andExpect(jsonPath("$.requestCount").value(8L));
    }

    @Test
    @DisplayName("Test to test the invocation of statistic service with correct values return 200")
    public void callServiceWithCorrectParameter() throws Exception {
        // Dados de prueba
        int value = 5;
        FibonacciStatisticResponse response = FibonacciStatisticResponse.builder()
                .number(value)
                .requestCount(8)
                .build();

        when(fibonacciStatisticService.getStatisticByNumber(value)).thenReturn(Optional.of(response));

        // Ejecuta el test
        mockMvc.perform(get("/api/v1/statistic/{number}", value));

        // Verifica que el servicio fue llamado con el parámetro correcto
        verify(fibonacciStatisticService).getStatisticByNumber(value);
    }

}
