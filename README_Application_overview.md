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





### Introduction to the Design and Implementation of the `/weather/latest` Endpoint

The `/weather/latest` endpoint is designed to fetch the most recent weather data for a specified city and country. This endpoint is part of a weather application built with Spring Boot, integrating various components such as data retrieval, rate limiting, and exception handling to provide a robust and reliable service. Below is an overview of the design and implementation aspects of this endpoint.

#### Design Overview

### Overview of the Two Endpoints and Their Functionality

1. **GET /weather**
   - **Functionality**: This endpoint retrieves the current weather data for a specified city and country using the OpenWeatherMap API.
   - **Parameters**:
      - `city`: The name of the city for which the weather data is requested.
      - `country`: The country code (ISO 3166-1 alpha-2) corresponding to the city.
      - `apiKey`: The API key provided in the request header to authenticate the request.
   - **Process**:
      - The controller calls the `WeatherService` to fetch the weather data from the OpenWeatherMap API using the provided city, country, and API key.
      - If the request exceeds the rate limit, a `RateLimitExceededException` is thrown.
      - If the request is valid and within the rate limit, the weather data is retrieved and returned in the response.
      - In case of validation errors, a `BAD_REQUEST` status is returned with details of the validation issues.

2. **GET /weather/latest**
   - **Functionality**: This endpoint retrieves the latest weather data from the H2 database for a specified city and country.
   - **Parameters**:
      - `city`: The name of the city for which the weather data is requested.
      - `country`: The country code (ISO 3166-1 alpha-2) corresponding to the city.
   - **Process**:
      - The controller calls the `WeatherService` to fetch the latest weather data from the H2 database using the provided city and country.
      - If no data is found, a `RuntimeException` is thrown with a message indicating that the result was not found for the specified city and country.
      - If data is found, it is returned in the response.

### Example Usage

**GET /weather**
- **Request**:
  ```
  GET /weather?city=London&country=UK
  Header: X-API-KEY: YOUR_API_KEY
  ```
- **Response** (Successful):
  ```json
  {
    "coord": {"lon": -0.1257, "lat": 51.5085},
    "weather": [{"id": 804, "main": "Clouds", "description": "overcast clouds", "icon": "04n"}],
    "main": {"temp": 287.42, "feels_like": 287.17, "temp_min": 286.01, "temp_max": 288.58, "pressure": 1017, "humidity": 87},
    "name": "London",
    "cod": 200
  }
  ```
- **Response** (Rate Limit Exceeded):
  ```json
  {
    "status": 429,
    "error": "Too Many Requests",
    "message": "Hourly rate limit has been exceeded for the given API key"
  }
  ```

**GET /weather/latest**
- **Request**:
  ```
  GET /weather/latest?city=London&country=UK
  ```
- **Response** (Successful):
  ```json
  {
    "coord": {"lon": -0.1257, "lat": 51.5085},
    "weather": [{"id": 804, "main": "Clouds", "description": "overcast clouds", "icon": "04n"}],
    "main": {"temp": 287.42, "feels_like": 287.17, "temp_min": 286.01, "temp_max": 288.58, "pressure": 1017, "humidity": 87},
    "name": "London",
    "cod": 200
  }
  ```
- **Response** (No Data Found):
  ```json
  {
    "status": 404,
    "error": "Not Found",
    "message": "Result not found for city London, in country UK"
  }
  ```

These endpoints provide essential weather data functionality, integrating external API requests with rate limiting and local database queries for efficient data retrieval.

#### Implementation Details


### Conclusion

The `/weather/latest` endpoint is a well-designed and implemented feature that leverages Spring Boot’s capabilities to provide weather data retrieval functionality. It ensures robustness through comprehensive error handling and rate limiting, enhancing the reliability and user experience of the weather application.


#### Implementation Details


1. **WeatherController**:
    - The controller exposes the `/weather` endpoint which accepts city, country, and API key as inputs.
    - The controller exposes the `/weather/latest` endpoint which accepts city, country, and API key as inputs.
    - If the rate limit is not exceeded, it calls the `WeatherService` to fetch weather data.
2. **WeatherService**:
    - Uses Spring WebClient to send HTTP GET requests to the OpenWeatherMap API.
    - Maps the JSON response to the `WeatherResponse` DTO.
    - Provides the fetched data back to the controller for response to the client.

3. **Rate Limiting**:
    - Implemented using a token-bucket algorithm to track and control the number of requests.
    - Each API key is assigned a bucket that allows up to 5 tokens (requests) per hour.
    - Tokens are consumed with each request, and a request is denied if no tokens are available.
### Overview of the Rate Limiting Pattern Deployed

The rate limiting pattern used in this implementation is based on the Token Bucket algorithm. This pattern helps to control the rate at which requests are processed, ensuring that the API is not overwhelmed by too many requests in a short period.

#### Token Bucket Algorithm
- **Concept**: The Token Bucket algorithm is a simple and efficient way to handle rate limiting. Tokens are added to the bucket at a fixed rate. Each incoming request consumes one token from the bucket. If there are no tokens available, the request is denied.
- **Parameters**:
    - **Bucket Capacity**: The maximum number of tokens that the bucket can hold.
    - **Token Refill Rate**: The rate at which tokens are added to the bucket.
    - **Filter Integration**: A `RateLimiterFilter` is implemented to intercept all incoming requests and apply the rate limiting logic. This ensures that rate limiting is consistently enforced across all endpoints.

#### Benefits
- **Scalability**: The Token Bucket algorithm is highly scalable and can handle large volumes of requests efficiently.
- **Fairness**: Each API key is independently rate-limited, ensuring fair usage among multiple clients.
- **Flexibility**: The rate limiting parameters (capacity and refill rate) can be easily adjusted based on usage patterns and requirements.

#### Conclusion
The rate limiting pattern deployed using the Token Bucket algorithm provides a robust mechanism to control the rate of incoming requests, ensuring that the system remains stable and responsive. By integrating this logic into a filter, the application maintains clean and consistent rate limiting across all endpoints.

4. **Exception Handling**:
    - `handleValidationExceptions` method manages validation errors and returns appropriate error messages.
    - `handleRateLimitExceededException` method catches rate limit exceptions and returns a 429 status code with an error message.

#### Testing

- **Unit Tests**: Verify the logic of individual components like WeatherService using mock responses.
- **Integration Tests**: Use `MockMvc` to simulate HTTP requests to the controller and ensure the correct handling of requests, responses, and exceptions.
- **Rate Limiting Tests**: Ensure that the rate limiter correctly blocks requests when the limit is exceeded.

This structured approach ensures the Weather Application is well-designed, modular, and easy to extend with additional features or improvements in the future. The use of Spring Boot and associated technologies allows for rapid development and deployment, while robust error handling and rate limiting provide a reliable user experience.