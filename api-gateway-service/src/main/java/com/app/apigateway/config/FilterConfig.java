package com.app.apigateway.config;

import com.app.apigateway.filter.SimpleCorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Filter configuration to explicitly register filters with proper ordering.
 */
@Configuration
public class FilterConfig {

    /**
     * Registers SimpleCorsFilter with highest precedence to run before Spring Security.
     */
    @Bean
    public FilterRegistrationBean<SimpleCorsFilter> corsFilterRegistration() {
        FilterRegistrationBean<SimpleCorsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SimpleCorsFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(-100); // Highest precedence (lower number = earlier)
        registration.setName("simpleCorsFilter");
        return registration;
    }
}
