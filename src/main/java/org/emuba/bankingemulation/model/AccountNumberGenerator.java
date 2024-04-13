package org.emuba.bankingemulation.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class AccountNumberGenerator {

    private static final int ACCOUNT_NUMBER_LENGTH = 16;
    private static final Random random = new Random();
    private static final Set<String> generatedNumbers = new HashSet<>();

    public static String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = generateRandomAccountNumber();
        } while (!generatedNumbers.add(accountNumber));
        return accountNumber;
    }

    private static String generateRandomAccountNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
