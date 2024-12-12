package com.christmas.letter.sender.controller;

import com.christmas.letter.sender.model.ChristmasLetter;
import com.christmas.letter.sender.service.ChristmasLetterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChristmasLetterController.class)
public class ChristmasLetterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private ChristmasLetterService christmasLetterService;

    private static final String PATH = "/letters/send";

    @Test
    public void whenSendChristmasLetter_withValidRequest_ThenMessageReceivedOnQueue() throws Exception {
        //Prepare message for the SNS Topic
        String testMessage = objectMapper.writeValueAsString(new ChristmasLetter("testing@gmail.com", "Luiza", "fimo", "Iasi"));

        //Mock sending the message
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testMessage))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("generateInvalidInput")
    void whenSendChristmasLetter_withInvalidRequest_ThenReturnBadRequest(ChristmasLetter christmasLetter) throws Exception {
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(christmasLetter)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The validation has failed"));
    }

    private static Stream<Arguments> generateInvalidInput(){
        return Stream.of(
                Arguments.of(new ChristmasLetter("invalidEmail", "Luiza", "fimo", "Iasi")),
                Arguments.of(new ChristmasLetter("test@gmail.com", "", "a dog", "Iasi")),
                Arguments.of(new ChristmasLetter("test@gmail.com", "Luiza", "", "Iasi")),
                Arguments.of(new ChristmasLetter("test@gmail.com", "Luiza", "books", ""))
        );
    }

}
