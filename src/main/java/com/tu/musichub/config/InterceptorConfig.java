package com.tu.musichub.config;

import com.tu.musichub.song.interceptors.SongManagementInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private static final String DELETE_SONG_URL_PATTERN = "/songs/delete/{id}";

    private static final String EDIT_SONG_URL_PATTERN = "/songs/edit/{id}";

    private final SongManagementInterceptor songManagementInterceptor;

    @Autowired
    public InterceptorConfig(SongManagementInterceptor songManagementInterceptor) {
        this.songManagementInterceptor = songManagementInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.songManagementInterceptor)
                .addPathPatterns(DELETE_SONG_URL_PATTERN, EDIT_SONG_URL_PATTERN);
    }
}
