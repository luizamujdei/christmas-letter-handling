package com.christmas.letter.processor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class ChristmasLetterEntity {

    private String email;

    private String name;

    private String wishes;

    private String location;

    public ChristmasLetterEntity(Map<String, AttributeValue> letterValueMap){
        this.email = getOrElse(letterValueMap, "email");

        this.name = getOrElse(letterValueMap, "name");

        this.wishes = getOrElse(letterValueMap, "wishes");

        this.location = getOrElse(letterValueMap, "location");

    }

    private static String getOrElse(Map<String, AttributeValue> letterValueMap, String key) {
        return Optional.ofNullable(letterValueMap.get(key))
                .map(AttributeValue::s)
                .orElse("");
    }


}
