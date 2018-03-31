package com.softuni.musichub.util;

import java.util.UUID;

public class UUIDUtil {

    public static String getRandomUUID() {
        String randomUUID = UUID.randomUUID().toString();
        return randomUUID;
    }
}
