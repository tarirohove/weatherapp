package com.vanguard.controller;

import com.vanguard.model.Sys;
import com.vanguard.model.Weather;
import com.vanguard.model.WeatherData;
import com.vanguard.service.RateLimitingService;
import com.vanguard.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
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

    @Test
    public void testGetWeather() throws Exception {
        when(weatherService.fetchWeatherInformation(eq("London"), eq("UK"), Mockito.anyString())).thenReturn("clear sky");
        when(rateLimiter.allowRequest(anyString())).thenReturn(Boolean.TRUE);
        mockMvc.perform(get("/weather")
                        .param("city", "London")
                        .param("country", "UK")
                        .header("X-API-KEY", "validApiKey"))
                .andExpect(status().isOk())
                .andExpect(content().string("clear sky"));
    }

    @Test
    public void whenInvalidRequestParam_thenReturns400() throws Exception {
        when(weatherService.fetchWeatherInformation(eq("London"), eq("UK"), Mockito.anyString())).thenReturn("clear sky");

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
    public void testGetLatestWeather_Success() throws Exception {
        Weather weather = new Weather();
        weather.setDescription("Partly Cloudy");

        Sys sys = new Sys();
        sys.setCountry("UK");

        WeatherData weatherData = new WeatherData();
        weatherData.setWeather(List.of(weather));
        weatherData.setSys(sys);
        weatherData.setName("London");

        when(weatherService.getLatestWeatherData("London", "UK")).thenReturn(Optional.of(weatherData));

        mockMvc.perform(MockMvcRequestBuilders.get("/weather/latest")
                        .param("city", "London")
                        .param("country", "UK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sys.country").value("UK"))
                .andExpect(jsonPath("$.weather[0].description").value("Partly Cloudy"))
                .andExpect(jsonPath("$.name").value("London"));
    }

    @Test
    public void testGetLatestWeather_NotFound() throws Exception {
        when(weatherService.getLatestWeatherData("NonExistentCity", "XX")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/weather/latest")
                        .param("city", "NonExistentCity")
                        .param("country", "XX"))
                .andExpect(status().isOk())
                .andExpect(content().string("There are no weather results for city: NonExistentCity, in country: XX"));
    }

    @Test
    public void testGetLatestWeather_MissingParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/latest")
                        .param("city", ""))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.get("/weather/latest")
                        .param("country", ""))
                .andExpect(status().isBadRequest());
    }
}
