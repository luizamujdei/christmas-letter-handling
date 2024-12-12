package com.christmas.letter.sender.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChristmasLetter {

    @Email(message = "Email should be valid")
    @NotNull(message = "Email is a mandatory field")
    private String email;

    @NotEmpty(message = "Name is a mandatory field")
    private String name;

    @NotEmpty(message = "Wishes is a mandatory field")
    private String wishes;

    @NotEmpty(message = "Location is a mandatory field")
    private String location;
}
