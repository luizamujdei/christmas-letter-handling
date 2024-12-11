package com.christmas.letter.sender.service;

import com.christmas.letter.sender.model.ChristmasLetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChristmasLetterService {

    private final SnsClient snsClient;

    @Value("${sns.topic.arn}")
    private String topicArn;

    public String sendChristmasLetter(ChristmasLetter letter) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String message = objectMapper.writeValueAsString(letter);

            //Publish to the SNS topic
            PublishRequest request = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(message)
                    .build();

            PublishResponse response = snsClient.publish(request);

            return "Message published to the  topic with message id" + response.messageId();

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to publish the letter", e);
        }

    }
}
