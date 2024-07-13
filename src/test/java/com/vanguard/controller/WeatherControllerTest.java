package com.vanguard.controller;

import com.vanguard.model.WeatherData;
import com.vanguard.repository.WeatherDataRepository;
import com.vanguard.service.RateLimitingService;
import com.vanguard.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private RateLimitingService rateLimiter;

    @MockBean
    private WeatherDataRepository repository;


    @Test
    public void testGetWeather() throws Exception {
        Mockito.when(weatherService.fetchWeatherInformation(eq("London"), eq("UK"), Mockito.anyString())).thenReturn("clear sky");
        Mockito.when(rateLimiter.allowRequest(anyString())).thenReturn(Boolean.TRUE);
        mockMvc.perform(get("/weather")
                        .param("city", "London")
                        .param("country", "UK")
                .header("X-API-KEY", "validApiKey"))
                .andExpect(status().isOk())
                .andExpect(content().string("clear sky"));
    }

    @Test
    public void whenInvalidRequestParam_thenReturns400() throws Exception {
        Mockito.when(weatherService.fetchWeatherInformation(eq("London"), eq("UK"), Mockito.anyString())).thenReturn("clear sky");

        ResultActions result = mockMvc.perform(get("/weather")
                        .param("invalidRequestParam0", "London")
                        .param("invalidRequestParam1", "UK")
                .header("AUTH_TOKEN_HEADER_NAME", "validApiKey"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Required request parameter 'city' for method parameter type String is not present"));
    }

    @Test
    public void whenInvalidTokenHeader_thenReturns400() throws Exception {
        ResultActions result = mockMvc.perform(get("/weather")
                .param("city", "London")
                .param("country", "UK")
                .header("INVALID_TOKEN_HEADER_NAME", "validApiKey"));

        result.andExpect(status().isBadRequest())
                .andExpect(content().string("Required request header 'X-API-KEY' for method parameter type String is not present"));

    }

    @Test
    public void whenRateLimitExceeded_thenReturns429() throws Exception {
        Mockito.when(rateLimiter.allowRequest(anyString())).thenReturn(Boolean.FALSE);

        mockMvc.perform(get("/weather")
                        .param("city", "London")
                        .param("country", "UK")
                        .header("X-API-KEY", "dummyApiKey"))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string("Hourly rate limit exceeded"));
    }
}
