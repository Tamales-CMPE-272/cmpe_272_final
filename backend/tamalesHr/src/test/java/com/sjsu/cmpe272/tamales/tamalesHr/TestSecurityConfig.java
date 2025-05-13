package com.sjsu.cmpe272.tamales.tamalesHr;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/auth/**").permitAll()  // ✅ allow /auth/**
                        .anyRequest().authenticated()  // ✅ keep other endpoints protected
                )
                .oauth2ResourceServer().disable();;
        return http.build();
    }
}
