package com.softuni.musichub.user.utils;

import java.util.UUID;

public class UUIDUtil {

    public static String getRandomUUID() {
        String randomUUID = UUID.randomUUID().toString();
        return randomUUID;
    }
}
