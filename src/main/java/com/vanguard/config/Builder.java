package com.vanguard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class Builder {

    @Bean
    public WebClient.Builder webClientBuilder() {
      return   WebClient.builder().baseUrl("");

    }
}
