package com.christmas.letter.processor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChristmasLetter {

    private String email;

    private String name;

    private String wishes;

    private String location;
}
