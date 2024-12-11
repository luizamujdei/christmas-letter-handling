package com.christmas.letter.sender.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ChristmasLetter {

    @Email
    @NotNull(message = "Email is a mandatory field")
    private String email;

    @NotEmpty(message = "Name is a mandatory field")
    private String name;

    @NotEmpty(message = "Wishes is a mandatory field")
    private String wishes;

    @NotEmpty(message = "Location is a mandatory field")
    private String location;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWishes() {
        return wishes;
    }

    public void setWishes(String wishes) {
        this.wishes = wishes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ChristmasLetter(String email, String name, String wishes, String location) {
        this.email = email;
        this.name = name;
        this.wishes = wishes;
        this.location = location;
    }
}
