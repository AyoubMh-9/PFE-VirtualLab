package com.virtual.lab.backend.util;

import java.security.SecureRandom;
import java.time.Year;

public class CodeGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SUFFIX_LENGTH = 3; // ex : "X9K"
    private static final SecureRandom random = new SecureRandom();

    public static String generateAccessCode() {
        StringBuilder suffix = new StringBuilder(SUFFIX_LENGTH);
        for (int i = 0; i < SUFFIX_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            suffix.append(CHARACTERS.charAt(index));
        }
        String year = String.valueOf(Year.now().getValue()).substring(2); // "25" pour 2025
        return "QTP.P" + year + suffix;
    }
}

