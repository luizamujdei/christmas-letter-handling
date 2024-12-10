package com.christmas.letter.sender.controller;

import com.christmas.letter.sender.ChristmasLetter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.PublishRequest;


@RestController
@RequestMapping("/letters")
public class ChristmasLetterController {

  private final SnsClient snsClient;

  @Value("${sns.topic.arn}")
  private String topicArn;

  public ChristmasLetterController(SnsClient snsClient){
    this.snsClient = snsClient;
  }

  @PostMapping("/send")
  public String sendChristmasLetter(@RequestBody ChristmasLetter letter) {

    String message = String.format("Email: %s\nName: %s\nWishes: %s\nLocation: %s",
            letter.getEmail(), letter.getName(), letter.getWishes(), letter.getLocation());

    //Publish to the SNS topic
    PublishRequest request = PublishRequest.builder()
            .topicArn(topicArn)
            .message(message)
            .build();

    PublishResponse response = snsClient.publish(request);

    return "Message published to the  topic with message id" + response.messageId();
  }

}
