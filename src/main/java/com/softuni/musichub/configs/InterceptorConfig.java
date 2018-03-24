package com.softuni.musichub.configs;

import com.softuni.musichub.song.interceptors.SongManagementInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final SongManagementInterceptor songInterceptor;

    @Autowired
    public InterceptorConfig(SongManagementInterceptor songInterceptor) {
        this.songInterceptor = songInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.songInterceptor)
                .addPathPatterns("/songs/delete/{id}", "/songs/edit/{id}");
    }
}
