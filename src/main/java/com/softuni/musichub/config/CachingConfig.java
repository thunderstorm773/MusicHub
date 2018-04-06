package com.softuni.musichub.config;

import com.softuni.musichub.staticData.Constants;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@CacheConfig(cacheNames = Constants.SONGS_CACHE_NAME)
public class CachingConfig {
}
