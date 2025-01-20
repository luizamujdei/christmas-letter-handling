package com.christmas.letter.processor.service;

import com.christmas.letter.processor.exception.NotFoundException;
import com.christmas.letter.processor.model.CachedPage;
import com.christmas.letter.processor.model.ChristmasLetter;
import com.christmas.letter.processor.repository.ChristmasLetterRepository;
import com.christmas.letter.processor.utils.LetterTestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class ChristmasLetterProcessorServiceTest {
    @Mock
    private ChristmasLetterRepository letterRepository;

    @InjectMocks
    private ChristmasLetterProcessorService letterProcessorService;

    @Test()
    void whenGetLetterByNonExistentEmail_thenThrowNotFoundException() {
        String email = "non_existent@test.com";
        when(letterRepository.findById(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> letterProcessorService.getLetterByEmail(email))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("No letter found for email: %s", email));
    }

    @Test()
    void whenValidRequestGetLetterByEmail_thenReturnCorrectLetter() {

        String email = "good_kid@test.com";
        ChristmasLetter letter = LetterTestHelper.createLetter();
        when(letterRepository.findById(email)).thenReturn(Optional.of(letter));

        ChristmasLetter result = letterProcessorService.getLetterByEmail(email);

        assertThat(result).isEqualTo(letter);
    }

    @Test()
    void whenValidRequestGetLetters_thenReturnSavedLetters() {

        List<ChristmasLetter> letters = List.of(LetterTestHelper.createLetter());
        Page<ChristmasLetter> savedPage = new PageImpl<>(letters);
        Pageable pageable = PageRequest.of(0, 5);
        when(letterRepository.findAll(pageable)).thenReturn(savedPage);

        CachedPage<ChristmasLetter> result = letterProcessorService.getLetters(pageable);

        assertThat(result.getTotalElements()).isOne();
    }

}