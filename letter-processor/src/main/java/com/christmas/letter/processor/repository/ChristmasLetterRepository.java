package com.christmas.letter.processor.repository;

import com.christmas.letter.processor.model.ChristmasLetter;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBPagingAndSortingRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.stereotype.Repository;

@Repository
@EnableScan
@EnableScanCount
public interface ChristmasLetterRepository extends DynamoDBCrudRepository<ChristmasLetter, String>, DynamoDBPagingAndSortingRepository<ChristmasLetter, String> {

}
