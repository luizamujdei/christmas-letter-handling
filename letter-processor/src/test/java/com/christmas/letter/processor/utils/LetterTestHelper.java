package com.christmas.letter.processor.utils;

import com.christmas.letter.processor.model.ChristmasLetter;
import lombok.experimental.UtilityClass;
import net.bytebuddy.utility.RandomString;

@UtilityClass
public class LetterTestHelper {

    public static ChristmasLetter createLetter() {
        String email = String.format("%s@test.com", RandomString.make());
        String name = RandomString.make();
        String wishes = RandomString.make();
        String location = RandomString.make();

        return new ChristmasLetter(email, name, wishes, location);
    }
}