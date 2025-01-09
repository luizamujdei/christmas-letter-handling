package com.christmas.letter.processor.config;

import com.christmas.letter.processor.model.mapper.SqsMessageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Data
@Configuration
public class AwsConfigProperties {

    @Value("${aws.access.key.id}")
    private String accessKeyId;

    @Value("${aws.secret.access.key}")
    private String secretKeyId;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.sns.endpoint.url}")
    private String snsEndpointUrl;

    @Bean
    public DynamoDbClient dynamoDbClient(){
        return DynamoDbClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(snsEndpointUrl))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretKeyId)))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(){
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient())
                .build();
    }

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient, ObjectMapper objectMapper){
        return SqsMessageListenerContainerFactory.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .configure(options -> options.messageConverter(new SqsMessageMapper(objectMapper)))
                .build();
    }
}
