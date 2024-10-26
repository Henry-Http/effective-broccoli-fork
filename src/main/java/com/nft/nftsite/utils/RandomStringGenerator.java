package com.nft.nftsite.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;

public class RandomStringGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String generateRandomString(int length, boolean onlyUppercase) {
        return RandomStringUtils.randomAlphanumeric(length).toUpperCase();
    }

    public static String generateSlug(String title) {
        String slug = title.replaceAll("[^A-Za-z0-9]+", "-");
        slug = slug.replaceAll("^[^A-Za-z0-9]+", "");
        slug = slug.replaceAll("[^A-Za-z0-9]+$", "");

        String randomSuffix = RandomStringGenerator.generateRandomString(6);
        slug = slug.toLowerCase() + "-" + randomSuffix.toLowerCase();
        return slug;
    }
}
