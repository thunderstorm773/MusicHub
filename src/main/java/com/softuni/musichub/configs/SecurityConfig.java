package com.softuni.musichub.configs;

import com.softuni.musichub.user.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final int TOKEN_VALIDITY_SECONDS = 864_000;

    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserService userService,
                          BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/bootstrap-4.0.0.**", "/css/**",
                        "/font-awesome/**", "/images/**",
                        "/jquery/**", "/theme/**").permitAll()
                .antMatchers("/users/login", "/users/register").anonymous()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/music/**").authenticated()
                .and().exceptionHandling().accessDeniedPage("/")
                .and().formLogin().defaultSuccessUrl("/").loginPage("/users/login")
                .usernameParameter("username").passwordParameter("password")
                .and().rememberMe()
                .rememberMeParameter("rememberMe")
                .tokenValiditySeconds(TOKEN_VALIDITY_SECONDS)
                .and().logout().logoutUrl("/users/logout")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userService)
                .passwordEncoder(this.passwordEncoder);
    }
}
