package com.christmas.letter.processor.controller;

import com.christmas.letter.processor.model.ChristmasLetter;
import com.christmas.letter.processor.repository.ChristmasLetterRepository;
import com.christmas.letter.processor.utils.LetterTestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(LetterProcessorController.class)
public class LetterProcessorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChristmasLetterRepository letterRepository;

    private static final String PATH = "/letters";

    @Test
    void whenInvalidEmailProvided_thenThrowValidationException() throws Exception {
        String email = "invalid";

        mockMvc.perform(get(String.format("%s/{email}", PATH), email))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The validation has failed"));
    }


    @ParameterizedTest
    @MethodSource("generateLetters")
    void whenGivenPage_thenReturnPageLetters(Pageable pageable, List<ChristmasLetter> letters) throws Exception {
        letterRepository.saveAll(letters);

        mockMvc.perform(get(PATH)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(letters.size()))
                .andExpect(jsonPath("$.pageSize").value(pageable.getPageSize()))
                .andExpect(jsonPath("$.pageNumber").value(pageable.getPageNumber()))
                .andExpect(jsonPath("$.offset").value(pageable.getOffset()));
    }

    @Test
    void whenUnsavedEmailProvided_thenThrowNotFoundException() throws Exception {
        String email = "unsaved@test.com";

        mockMvc.perform(get(String.format("%s/{email}", PATH), email))
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format("Letter of email %s not found!", email)));
    }

    private static Stream<Arguments> generateLetters() {
        return Stream.of(
                Arguments.of(PageRequest.of(0, 8), getNewLetters(1)),
                Arguments.of(PageRequest.of(1, 2), getNewLetters(3))
        );
    }

    private static List<ChristmasLetter> getNewLetters(int count) {
        return IntStream.range(0, count).mapToObj(el -> LetterTestHelper.createLetter()).toList();
    }
}