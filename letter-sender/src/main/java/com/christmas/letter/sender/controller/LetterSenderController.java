package com.christmas.letter.sender.controller;

import com.christmas.letter.sender.model.ChristmasLetter;
import com.christmas.letter.sender.service.ChristmasLetterSenderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/letters")
@RequiredArgsConstructor
public class LetterSenderController {

    private final ChristmasLetterSenderService christmasLetterSenderService;

    @PostMapping("/send")
    public ResponseEntity<String> sendChristmasLetter(@Valid @RequestBody ChristmasLetter letter) {
        christmasLetterSenderService.sendChristmasLetter(letter);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
