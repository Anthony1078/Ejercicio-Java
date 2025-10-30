package com.example.eval_java.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class IdempotencyConfig {

    private final IdempotencyFilter filter;

    @Bean
    public FilterRegistrationBean<IdempotencyFilter> idempotencyFilterRegistration() {
        var reg = new FilterRegistrationBean<>(filter);
        reg.setOrder(10);
        reg.addUrlPatterns("/api/v1/users");
        return reg;
    }
}
