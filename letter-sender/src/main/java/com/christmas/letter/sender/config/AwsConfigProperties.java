package com.christmas.letter.sender.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix="aws.sns")
@Getter
@Setter
@Validated
public class AwsConfigProperties {

    @NotBlank(message = "SNS Topic Arn is missing")
    private String topicArn;
}
