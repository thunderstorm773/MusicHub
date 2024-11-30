package com.tu.musichub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CdnConfigData {

    public static final String CLOUD_NAME_KEY = "cloud_name";

    public static final String API_KEY = "api_key";

    public static final String API_SECRET_KEY = "api_secret";

    @Value("${CLOUD_NAME}")
    private String cloudName;

    @Value("${API_KEY}")
    private String apiKey;

    @Value("${API_SECRET}")
    private String apiSecret;

    public String getCloudName() {
        return this.cloudName;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getApiSecret() {
        return this.apiSecret;
    }
}
