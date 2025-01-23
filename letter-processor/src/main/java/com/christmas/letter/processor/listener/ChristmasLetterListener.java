package com.christmas.letter.processor.listener;

import com.christmas.letter.processor.model.ChristmasLetter;
import com.christmas.letter.processor.repository.ChristmasLetterRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.annotation.SqsListenerAcknowledgementMode;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
public class ChristmasLetterListener {

    private final ChristmasLetterRepository christmasLetterRepository;

    @SqsListener(value="${aws.sqs.queue.url}",
            maxConcurrentMessages = "10",
            acknowledgementMode = SqsListenerAcknowledgementMode.MANUAL)
    @Caching(evict = {@CacheEvict(value = "60m-letters", allEntries = true)},
            put = {@CachePut(value = "60m-letter", key = "#christmasLetter.email")})
    public void christmasLetterListener(@Valid @Payload ChristmasLetter christmasLetter, Acknowledgement acknowledgement) {
        christmasLetterRepository.save(christmasLetter);
        acknowledgement.acknowledge();
    }
}
