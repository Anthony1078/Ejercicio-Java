package com.example.eval_java.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.cors.*;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origins:}") String origins,
            @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}") String methods,
            @Value("${app.cors.allowed-headers:*}") String headers,
            @Value("${app.cors.allow-credentials:false}") boolean creds) {

        var cfg = new CorsConfiguration();
        if (!origins.isBlank()) cfg.setAllowedOrigins(Arrays.asList(origins.split(",")));
        cfg.setAllowedMethods(Arrays.asList(methods.split(",")));
        cfg.setAllowedHeaders(Arrays.asList(headers.split(",")));
        cfg.setAllowCredentials(creds);
        cfg.setMaxAge(3600L);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
