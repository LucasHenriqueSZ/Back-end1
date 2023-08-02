package com.newGo.clubFerias.util;

import java.util.Random;

public abstract class LetterCodeGenerator {

    public static String generate() {
        String code = "";
        for (int i = 0; i < 3; i++)
            code += getRandomLetter();
        return code;
    }

    private static char getRandomLetter() {
        Random random = new Random();

        int numeberLetter = random.nextInt(26);

        char letraAleatoria = (char) (numeberLetter + 65);

        return letraAleatoria;
    }
}
