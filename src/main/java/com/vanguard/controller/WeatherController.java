package com.vanguard.controller;

import com.vanguard.exception.ResultsNotFoundException;
import com.vanguard.filter.RateLimiterFilter;
import com.vanguard.model.WeatherData;
import com.vanguard.service.RateLimitingService;
import com.vanguard.service.WeatherService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.Optional;

@RestController
public class WeatherController {
    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam @NotEmpty String city,
                             @RequestParam @NotEmpty String country,
                             @RequestHeader(AUTH_TOKEN_HEADER_NAME) @NotEmpty String apiKey) {
        return weatherService.fetchWeatherInformation(city, country, apiKey);
    }

    @GetMapping("/weather/latest")
    public ResponseEntity<?> getLatestWeather(@RequestParam String city, @RequestParam String country) {
        Optional<WeatherData> weatherData = weatherService.getLatestWeatherData(city, country);
        return weatherData.map(ResponseEntity::ok).orElseThrow(() ->
                new ResultsNotFoundException(String.format("There are no weather results for city: %s, in country: %s", city, country)));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class,MissingRequestValueException.class, HandlerMethodValidationException.class})
    public String handleValidationExceptions( Exception ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler({WebClientException.class})
    public String handleWebClientException(WebClientException ex) {
        return "Issue encountered in a downstream service. Try later.";
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ResultsNotFoundException.class})
    public String handleResultsNotFoundException(ResultsNotFoundException ex) {
         return ex.getMessage();
    }
}

