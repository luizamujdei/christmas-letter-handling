package com.christmas.letter.processor.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@DynamoDBTable(tableName = "christmas_letter")
public class ChristmasLetter {

    @DynamoDBHashKey(attributeName = "Email")
    private String email;

    @DynamoDBAttribute(attributeName = "Name")
    private String name;

    @DynamoDBAttribute(attributeName = "Wishes")
    private String wishes;

    @DynamoDBAttribute(attributeName = "Location")
    @DynamoDBTypeConvertedJson
    private String location;
}
