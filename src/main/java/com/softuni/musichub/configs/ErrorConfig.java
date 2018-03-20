package com.softuni.musichub.configs;

import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ErrorConfig implements WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory> {

    @Override
    public void customize(ConfigurableTomcatWebServerFactory factory) {
        ErrorPage notFoundError = new ErrorPage(HttpStatus.NOT_FOUND,
                "/errors/404");
        factory.addErrorPages(notFoundError);
    }
}
