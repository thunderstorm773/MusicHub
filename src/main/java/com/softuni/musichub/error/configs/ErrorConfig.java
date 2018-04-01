package com.softuni.musichub.error.configs;

import com.softuni.musichub.error.staticData.ErrorConstants;
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
                ErrorConstants.NOT_FOUND_ERROR_ROUTE);
        factory.addErrorPages(notFoundError);
    }
}
