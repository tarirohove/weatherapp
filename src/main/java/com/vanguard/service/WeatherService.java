package com.vanguard.service;


import com.vanguard.dto.WeatherResponse;
import com.vanguard.model.WeatherData;
import com.vanguard.repository.WeatherDataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class WeatherService {
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
    private final WebClient webClient;
    private final WeatherDataRepository weatherDataRepository;
    private final ModelMapper modelMapper;

    public WeatherService(WebClient.Builder webClientBuilder,
                          WeatherDataRepository weatherDataRepository,
                          ModelMapper modelMapper) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
        this.weatherDataRepository = weatherDataRepository;
        this.modelMapper = modelMapper;
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
            WeatherData weatherData = weatherDataRepository.save(this.modelMapper.map(response, WeatherData.class));
            return weatherData.getWeather().get(0).getDescription();
        }
        return null;
    }

    public Optional<WeatherData> getLatestWeatherData(String city, String country) {
        return weatherDataRepository.findFirstByNameOrderByIdDesc(city);
    }
}


