package com.tu.musichub.config;

import com.tu.musichub.song.staticData.SongConstants;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@CacheConfig(cacheNames = SongConstants.SONGS_CACHE_NAME)
public class CachingConfig {
}
