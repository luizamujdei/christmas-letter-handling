package com.christmas.letter.processor.repository;

import com.christmas.letter.processor.model.ChristmasLetterEntity;
import com.christmas.letter.processor.model.pagination.PaginatedRequest;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

@Repository
public class ChristmasLetterRepository{

    private final DynamoDbTemplate dynamoDbTemplate;
    private final DynamoDbClient dynamoDbClient;

    public ChristmasLetterRepository(DynamoDbTemplate dynamoDbTemplate, DynamoDbClient dynamoDbClient){
        this.dynamoDbTemplate = dynamoDbTemplate;
        this.dynamoDbClient = dynamoDbClient;
    }

    public ChristmasLetterEntity store(ChristmasLetterEntity entity){
        return dynamoDbTemplate.save(entity);
    }

    public ChristmasLetterEntity getLetterByEmail(String email){
        return dynamoDbTemplate.load(Key.builder().partitionValue(email).build(), ChristmasLetterEntity.class);
    }

    public ScanResponse getLetters(PaginatedRequest paginatedRequest){
        AttributeValue lastEmailRetrieved = paginatedRequest.getLastEmailRetrieved().get("email");

        ScanRequest.Builder requestBuilder = ScanRequest.builder()
                .tableName("ChristmasLetter")
                .limit(paginatedRequest.getPageLimit());

        if ( lastEmailRetrieved != null && !lastEmailRetrieved.s().isEmpty()){
            requestBuilder.exclusiveStartKey(paginatedRequest.getLastEmailRetrieved());
        }

        return dynamoDbClient.scan(requestBuilder.build());
    }
}
