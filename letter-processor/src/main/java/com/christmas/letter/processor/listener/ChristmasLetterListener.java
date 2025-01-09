package com.christmas.letter.processor.listener;

import com.christmas.letter.processor.model.ChristmasLetter;
import com.christmas.letter.processor.repository.ChristmasLetterRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
public class ChristmasLetterListener {

    private final ChristmasLetterRepository christmasLetterRepository;

    @SqsListener(value="${aws.sqs.queue.url}", maxConcurrentMessages = "10")
    public void christmasLetterListener(@Valid @Payload ChristmasLetter christmasLetter) {
        christmasLetterRepository.save(christmasLetter);
    }
}
