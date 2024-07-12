package com.vanguard.controller;

import com.vanguard.exception.RateLimitExceededException;
import com.vanguard.service.RateLimitingService;
import com.vanguard.service.WeatherService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
public class WeatherController {
    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    private final RateLimitingService rateLimiter;
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService, RateLimitingService rateLimiter) {
        this.weatherService = weatherService;
        this.rateLimiter = rateLimiter;
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam @NotEmpty String city,
                             @RequestParam @NotEmpty String country,
                             @RequestHeader(AUTH_TOKEN_HEADER_NAME) @NotEmpty String apiKey) {
        if (!rateLimiter.allowRequest(apiKey)) {
            throw new RateLimitExceededException("Hourly rate limit has been exceeded for the API key: " + apiKey);
        }
        return weatherService.fetchWeatherInformation(city, country, apiKey);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class,MissingRequestValueException.class})
    public String handleValidationExceptions( Exception ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler({RateLimitExceededException.class})
    public String handleRateLimitExceededException(RateLimitExceededException ex) {
        return "Hourly rate limit exceeded";
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler({WebClientException.class})
    public String handleWebClientException(WebClientException ex) {
        return "Issue encountered in a downstream service. Try later.";
    }
}

