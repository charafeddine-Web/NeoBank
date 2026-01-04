package com.neobank.util;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

public class AccountNumberGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate() {
        long epoch = Instant.now().getEpochSecond();
        String s1 = Long.toString(epoch, 36).toUpperCase();
        Random random = new Random();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }

        return "NEOB" + s1 + sb.toString().toUpperCase();
    }
}

