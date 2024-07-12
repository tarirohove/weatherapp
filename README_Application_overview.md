### Design and Implementation of the Weather Application

The Weather Application is designed to provide real-time weather data by interfacing with the OpenWeatherMap API. The application is built using the Spring Boot framework to create a robust and easy-to-maintain service. The primary features include fetching weather data based on city and country inputs, rate limiting to ensure API key usage compliance and proper error handling.

#### Design Overview

The design is centered around a RESTful web service architecture. The core components of the application include the WeatherController, WeatherService, DTO classes for handling API responses, and a custom rate limiting service for managing API request limits.

1. **WeatherController**:
    - Handles incoming HTTP requests.
    - Provides an endpoint `/weather` to fetch weather data.
    - Uses the rate limiting service to restrict excessive API calls.
    - Includes exception handling to manage validation errors and rate limits error handling.

2. **WeatherService**:
    - Contains the business logic for communicating with the OpenWeatherMap API.
    - Utilizes Spring WebClient for asynchronous and non-blocking HTTP requests.
    - Maps JSON responses to DTO classes for structured data handling.
    - Saves the response on the H2 table

3. **DTO Classes**:
    - Represent the JSON structure returned by the OpenWeatherMap API.
    - Include classes such as WeatherResponse, Coord, Weather, Main, Wind, Clouds, and Sys.
    - Facilitate easy mapping and access to weather data.

4. **RateLimiter**:
    - Implements a token-bucket algorithm to control the rate of API requests.
    - Ensures each API key is limited to a maximum of 5 requests per hour.
    - Throws a custom exception when the rate limit is exceeded.

#### Implementation Details

1. **WeatherController**:
    - The controller exposes the `/weather` endpoint which accepts city, country, and API key as inputs.
    - It checks the rate limit for the provided API key and throws a `RateLimitExceededException` if the limit is reached.
    - If the rate limit is not exceeded, it calls the `WeatherService` to fetch weather data.

2. **WeatherService**:
    - Uses Spring WebClient to send HTTP GET requests to the OpenWeatherMap API.
    - Maps the JSON response to the `WeatherResponse` DTO.
    - Provides the fetched data back to the controller for response to the client.

3. **Rate Limiting**:
    - Implemented using a token-bucket algorithm to track and control the number of requests.
    - Each API key is assigned a bucket that allows up to 5 tokens (requests) per hour.
    - Tokens are consumed with each request, and a request is denied if no tokens are available.

4. **Exception Handling**:
    - `handleValidationExceptions` method manages validation errors and returns appropriate error messages.
    - `handleRateLimitExceededException` method catches rate limit exceptions and returns a 429 status code with an error message.

#### Testing

- **Unit Tests**: Verify the logic of individual components like WeatherService using mock responses.
- **Integration Tests**: Use `MockMvc` to simulate HTTP requests to the controller and ensure the correct handling of requests, responses, and exceptions.
- **Rate Limiting Tests**: Ensure that the rate limiter correctly blocks requests when the limit is exceeded.

This structured approach ensures the Weather Application is well-designed, modular, and easy to extend with additional features or improvements in the future. The use of Spring Boot and associated technologies allows for rapid development and deployment, while robust error handling and rate limiting provide a reliable user experience.