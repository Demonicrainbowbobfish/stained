package io.sparkycreepster.custom;

import java.util.Random;

public class generateRandomBetween10 {
    private Random rand = new Random();

    public int getRangedInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }
}
