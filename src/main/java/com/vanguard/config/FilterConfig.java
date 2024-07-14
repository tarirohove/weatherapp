package com.vanguard.config;

import com.vanguard.filter.RateLimiterFilter;
import com.vanguard.service.RateLimitingService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RateLimiterFilter> rateLimiterFilter(RateLimitingService rateLimitingService) {
        FilterRegistrationBean<RateLimiterFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimiterFilter(rateLimitingService));
        registrationBean.addUrlPatterns("/*"); // Apply to all URL patterns
        return registrationBean;
    }
}

