package com.christmas.letter.processor.service;

import com.christmas.letter.processor.model.ChristmasLetter;
import com.christmas.letter.processor.model.ChristmasLetterEntity;
import com.christmas.letter.processor.model.ChristmasLetterMapper;
import com.christmas.letter.processor.model.pagination.PaginatedRequest;
import com.christmas.letter.processor.model.pagination.PaginatedResponse;
import com.christmas.letter.processor.repository.ChristmasLetterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ChristmasLetterProcessorService {

    private final ChristmasLetterRepository christmasLetterRepository;

    public ChristmasLetterProcessorService(ChristmasLetterRepository christmasLetterRepository){
        this.christmasLetterRepository = christmasLetterRepository;
    }

    @SqsListener(value="${aws.sqs.queue.url}", maxConcurrentMessages = "10")
    public void christmasLetterListener(Message message) throws JsonProcessingException {
        christmasLetterRepository.store(Objects.requireNonNull(ChristmasLetterMapper.INSTANCE.objectToEntity(new ObjectMapper().readValue(message.body(), ChristmasLetter.class))));
    }

    public ChristmasLetter getLetterByEmail(String email){
        return ChristmasLetterMapper.INSTANCE.entityToObject(christmasLetterRepository.getLetterByEmail(email));
    }

    public PaginatedResponse getLetters(PaginatedRequest paginatedRequest){
        ScanResponse response = christmasLetterRepository.getLetters(paginatedRequest);

        List<ChristmasLetter> letters = response.items().stream()
                .map(item -> ChristmasLetterMapper.INSTANCE.entityToObject(new ChristmasLetterEntity(item)))
                .toList();

        String lastReturnedEmail = Optional.ofNullable(response.lastEvaluatedKey())
                .map(keys -> keys.get("email"))
                .map(AttributeValue::s)
                .orElse("");

        return PaginatedResponse.builder()
                .letters(letters)
                .lastReturnedEmail(lastReturnedEmail)
                .build();
    }

}
