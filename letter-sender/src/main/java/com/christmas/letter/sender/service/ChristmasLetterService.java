package com.christmas.letter.sender.service;

import com.christmas.letter.sender.config.AwsConfigProperties;
import com.christmas.letter.sender.model.ChristmasLetter;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChristmasLetterService {

    private final SnsTemplate snsTemplate;

    private final AwsConfigProperties awsConfigProperties;

    public void sendChristmasLetter(ChristmasLetter letter) {
        final String topicArn = awsConfigProperties.getTopicArn();
        snsTemplate.convertAndSend(topicArn, letter);
    }
}
