package util;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import java.util.Random;

public class GeradorCodigoCarteirinha {

    public static String getCardCode() {
        Random random = new Random();

        char[] alphabetAndNumbers = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        return NanoIdUtils.randomNanoId(random, alphabetAndNumbers, 8);
    }
}
