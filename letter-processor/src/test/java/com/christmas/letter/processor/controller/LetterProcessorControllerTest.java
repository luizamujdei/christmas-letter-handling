package com.christmas.letter.processor.controller;

import com.christmas.letter.processor.service.ChristmasLetterProcessorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LetterProcessorController.class)
public class LetterProcessorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChristmasLetterProcessorService christmasLetterProcessorService;

    private static final String PATH = "/letters";

    @Test
    void whenInvalidEmailProvided_thenThrowValidationException() throws Exception {
        String email = "invalid";

        mockMvc.perform(get(String.format("%s/{email}", PATH), email))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The validation has failed"));
    }

}
