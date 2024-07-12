package com.vanguard.service;


import com.vanguard.dto.WeatherResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
    private final WebClient webClient;

    public WeatherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }
    public String fetchWeatherInformation(String city, String country, String apiKey) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("q", city + "," + country)
                .queryParam("appid", apiKey)
                .toUriString();

        WeatherResponse response = this.webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .block();

        if (response != null) {
            return response.getWeather().get(0).getDescription();
        }
        return null;
    }
}


