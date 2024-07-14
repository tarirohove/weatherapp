package com.vanguard.config;

import com.vanguard.filter.RateLimiterFilter;
import com.vanguard.service.RateLimitingService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class Config {
    @Bean
    public WebClient.Builder webClientBuilder() {
      return   WebClient.builder();
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
