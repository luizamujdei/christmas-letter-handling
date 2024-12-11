package com.christmas.letter.sender.controller;

import com.christmas.letter.sender.model.ChristmasLetter;
import com.christmas.letter.sender.service.ChristmasLetterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

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

    @Test
    public void whenSendChristmasLetter_withInvalidRequest_ThenReturnBadRequest() throws Exception {
        //Prepare message for the SNS Topic
        String invalidTestMessage = objectMapper.writeValueAsString(new ChristmasLetter("testing", "Luiza", "", ""));

        //Mock sending the message
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTestMessage))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The validation has failed"));
    }

}
