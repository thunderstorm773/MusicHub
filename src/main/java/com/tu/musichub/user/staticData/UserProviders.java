package com.tu.musichub.user.staticData;

public enum UserProviders {

    MUSIC_HUB("MusicHub"), GOOGLE("Google");

    private final String providerName;

    UserProviders(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return this.providerName;
    }
}
