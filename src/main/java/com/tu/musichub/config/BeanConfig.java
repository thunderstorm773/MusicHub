package com.tu.musichub.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tu.musichub.user.utils.UserUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BeanConfig {

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Gson getGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Bean
    public UserUtils userUtils() {
        return new UserUtils();
    }
}
