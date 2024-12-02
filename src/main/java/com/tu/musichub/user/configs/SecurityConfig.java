package com.tu.musichub.user.configs;

import com.tu.musichub.user.services.UserExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int TOKEN_VALIDITY_SECONDS = 864_000;

    private final UserExtractionService userExtractionService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;

    @Autowired
    public SecurityConfig(UserExtractionService userExtractionService,
                          BCryptPasswordEncoder passwordEncoder,
                          GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler) {
        this.passwordEncoder = passwordEncoder;
        this.userExtractionService = userExtractionService;
        this.googleOAuth2SuccessHandler = googleOAuth2SuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/bootstrap-4.0.0/**", "/css/**",
                        "/font-awesome/**", "/images/**",
                        "/jquery/**", "/theme/**", "/scripts/**", "/audiojs/**").permitAll()
                .antMatchers("/users/login", "/users/register", "/oauth2/authorization/google").anonymous()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/comments/approve/{id}", "/comments/reject/{id}", "/comments/pending")
                .hasAnyRole("ADMIN", "MODERATOR")
                .antMatchers("/tags/**").hasAnyRole("ADMIN", "MODERATOR")
                .antMatchers("/songs/upload", "/comments/post").authenticated()
                .anyRequest().permitAll()
                .and().exceptionHandling().accessDeniedPage("/")
                .and().formLogin().defaultSuccessUrl("/").loginPage("/users/login")
                .usernameParameter("username").passwordParameter("password")
                .and().rememberMe()
                .rememberMeParameter("rememberMe")
                .tokenValiditySeconds(TOKEN_VALIDITY_SECONDS)
                .and().logout().logoutUrl("/users/logout")
                .and().oauth2Login()
                            .loginPage("/oauth2/authorization/google")
                            .successHandler(this.googleOAuth2SuccessHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userExtractionService)
                .passwordEncoder(this.passwordEncoder);
    }
}
