package com.christmas.letter.processor.controller;

import com.christmas.letter.processor.model.ChristmasLetter;
import com.christmas.letter.processor.service.ChristmasLetterProcessorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/letters")
@RequiredArgsConstructor
public class LetterProcessorController {

    private final ChristmasLetterProcessorService christmasLetterProcessorService;

    @GetMapping("/{email}")
    public ChristmasLetter getLetterByEmail(@Valid @Email(message = "Email is not valid") @PathVariable String email){
        return christmasLetterProcessorService.getLetterByEmail(email);
    }

    @GetMapping
    public Page<ChristmasLetter> getLetters(@PageableDefault Pageable pageable){
        return christmasLetterProcessorService.getLetters(pageable);
    }

}
