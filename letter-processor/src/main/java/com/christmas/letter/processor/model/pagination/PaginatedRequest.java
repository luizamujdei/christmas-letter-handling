package com.christmas.letter.processor.model.pagination;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor
public class PaginatedRequest {

    private Integer pageLimit;

    private boolean previousPage;

    private Map<String, AttributeValue> lastEmailRetrieved = Map.of("email", AttributeValue.fromS(""));

    public PaginatedRequest(Integer pageLimit, String email, boolean previousPage){
        this.pageLimit = Optional.ofNullable(pageLimit).filter(limit -> limit > 0).orElse(10);
        this.lastEmailRetrieved = Optional.ofNullable(email)
                .map(e -> Map.of("email", AttributeValue.fromS(e)))
                .orElse(null);
        this.previousPage = previousPage;
    }
}
