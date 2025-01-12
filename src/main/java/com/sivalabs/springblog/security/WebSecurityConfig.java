package com.sivalabs.springblog.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final String[] PUBLIC_RESOURCES = {
        "/webjars/**",
        "/css/**",
        "/js/**",
        "/images/**",
        "/",
        "/login",
        "/forgot-password",
        "/reset-password",
        "/logout"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(c -> c.requestMatchers(PUBLIC_RESOURCES)
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/posts", "/posts/**")
                .permitAll()
                .anyRequest()
                .authenticated());
        http.formLogin(c -> c.loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .permitAll());
        http.logout(c -> c.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll()
                .logoutSuccessUrl("/"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
