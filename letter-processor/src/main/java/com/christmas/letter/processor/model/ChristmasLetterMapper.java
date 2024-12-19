package com.christmas.letter.processor.model;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChristmasLetterMapper {

    ChristmasLetterMapper INSTANCE = Mappers.getMapper(ChristmasLetterMapper.class);

    ChristmasLetter entityToObject(ChristmasLetterEntity christmasLetterEntity);
    ChristmasLetterEntity objectToEntity(ChristmasLetter christmasLetter);
}
