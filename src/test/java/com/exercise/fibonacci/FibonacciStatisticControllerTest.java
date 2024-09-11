package com.exercise.fibonacci;

import com.exercise.fibonacci.controllers.FibonacciStatisticController;
import com.exercise.fibonacci.dtos.response.FibonacciStatisticResponse;
import com.exercise.fibonacci.services.FibonacciStatisticService;
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
    public void getStatisticReturnOK() throws Exception {
        int value = 5;
        FibonacciStatisticResponse dto = FibonacciStatisticResponse.builder().number(value).requestCount(8).build();

        when(fibonacciStatisticService.getStatisticByNumber(value)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/v1/statistic/{number}",value))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(value))
                .andExpect(jsonPath("$.requestCount").value(8L));
    }

}
