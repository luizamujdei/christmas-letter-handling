package com.christmas.letter.processor.model.mapper;

import com.christmas.letter.processor.exception.ChristmasLetterDeserializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sqs.model.Message;

@RequiredArgsConstructor
public class SqsMessageMapper extends SqsMessagingMessageConverter {

    private final ObjectMapper objectMapper;

    @Override
    protected Object getPayloadToDeserialize(Message message){
        String body = message.body();

        try {
            JsonNode node = objectMapper.readTree(body);

            return node.path("Message").asText();
        } catch (JsonProcessingException e) {
            throw new ChristmasLetterDeserializationException("Exception encountered while deserializing the christmas letter");
        }

    }
}
