package com.app.items.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneratorRandomNumber {
    static final Random rand = new Random();

    public static int getRandomNumber() {
        return rand.nextInt(100) + 1;
    }
}
