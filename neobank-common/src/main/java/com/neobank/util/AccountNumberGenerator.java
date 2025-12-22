package com.neobank.util;

import java.security.SecureRandom;
import java.time.Instant;

public class AccountNumberGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate() {
        long epoch = Instant.now().getEpochSecond();
        String s1 = Long.toString(epoch, 36).toUpperCase();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int r = RANDOM.nextInt(36);
            sb.append(Character.forDigit(r, 36));
        }

        return "NEOB" + s1 + sb.toString().toUpperCase();
    }
}

