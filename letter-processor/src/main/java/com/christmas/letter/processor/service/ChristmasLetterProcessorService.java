package com.christmas.letter.processor.service;

import com.christmas.letter.processor.exception.NotFoundException;
import com.christmas.letter.processor.model.ChristmasLetter;
import com.christmas.letter.processor.repository.ChristmasLetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChristmasLetterProcessorService {

    private final ChristmasLetterRepository christmasLetterRepository;

    public ChristmasLetter getLetterByEmail(String email){
        return christmasLetterRepository.findById(email)
                .orElseThrow(() -> new NotFoundException(String.format("No letter found for email: %s", email)));
    }

    public Page<ChristmasLetter> getLetters(Pageable pageable){
        return christmasLetterRepository.findAll(pageable);
    }

}
