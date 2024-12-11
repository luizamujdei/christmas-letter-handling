package com.christmas.letter.sender.controller;

import com.christmas.letter.sender.model.ChristmasLetter;
import com.christmas.letter.sender.service.ChristmasLetterService;
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
public class ChristmasLetterController {

    private final ChristmasLetterService christmasLetterService;

    @PostMapping("/send")
    public ResponseEntity<String> sendChristmasLetter(@Valid @RequestBody ChristmasLetter letter) {
     String result = christmasLetterService.sendChristmasLetter(letter);
     return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}
