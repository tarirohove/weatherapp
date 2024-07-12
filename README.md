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

### Instructions to Execute the Weather App

#### Prerequisites

- **Java Development Kit (JDK) 11 or later**: Ensure you have JDK installed. You can download it from [Oracle JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or use an open-source alternative like [AdoptOpenJDK](https://adoptopenjdk.net/).
- **Maven**: Make sure you have Maven installed for dependency management and building the project. Download it from [Maven Download](https://maven.apache.org/download.cgi).
- **IDE**: An Integrated Development Environment like IntelliJ IDEA, Eclipse, or Visual Studio Code.
- **Internet Connection**: To fetch dependencies and access the OpenWeatherMap API.

#### Setup and Execution

1. **Clone the Repository**:
    - Clone the project repository to your local machine using Git:
      ```sh
      
      ```

2. **Configure API Key**:
    - Obtain an API key from [OpenWeatherMap](https://home.openweathermap.org/users/sign_up).
    - Update the `application.properties` file located in `src/main/resources` with your list of valid API keys comma separated:
      ```properties
      openweathermap.api.key.list=YOUR_API_KEY_FROM_OPEN_WEATHER_MAP
      ```

3. **Build the Project**:
    - Navigate to the project directory and build the project using Maven:
      ```sh
      mvn clean install
      ```

4. **Run the Application**:
    - Start the Spring Boot application using Maven:
      ```sh
      mvn spring-boot:run
      ```
    - Alternatively, you can run the generated JAR file:
      ```sh
      java -jar target/weather-application-0.0.1-SNAPSHOT.jar
      ```

5. **Access the Application**:
    - The application runs on port `8080` by default. You can access it via your web browser or a tool like Postman.
    - Endpoint to fetch weather data:
      ```
      GET http://localhost:8080/weather?city=London&country=GB
      ```
    - Include your API key in the request header:
      ```
      X-API-KEY: YOUR_API_KEY_STRING
      ```

#### Running Tests

1. **Unit and Integration Tests**:
    - Run the tests using Maven:
      ```sh
      mvn test
      ```

#### Example Requests

- **Fetch Weather Data**:
  ```sh
  curl -H "X-API-KEY: YOUR_API_KEY" "http://localhost:8080/weather?city=London&country=GB"
  ```

- **Expected JSON Response**:
  ```json
  {
      "coord": {
          "lon": -0.1257,
          "lat": 51.5085
      },
      "weather": [
          {
              "id": 804,
              "main": "Clouds",
              "description": "overcast clouds",
              "icon": "04n"
          }
      ],
      "base": "stations",
      "main": {
          "temp": 287.42,
          "feels_like": 287.17,
          "temp_min": 286.01,
          "temp_max": 288.58,
          "pressure": 1017,
          "humidity": 87,
          "sea_level": 1017,
          "grnd_level": 1014
      },
      "visibility": 10000,
      "wind": {
          "speed": 4.12,
          "deg": 30
      },
      "clouds": {
          "all": 100
      },
      "dt": 1720748915,
      "sys": {
          "type": 2,
          "id": 2075535,
          "country": "GB",
          "sunrise": 1720756663,
          "sunset": 1720815252
      },
      "timezone": 3600,
      "id": 2643743,
      "name": "London",
      "cod": 200
  }
  ```

#### Notes

- **Rate Limiting**: Ensure your API usage does not exceed the set rate limits (5 requests per hour per API key). The application will return a `429 Too Many Requests` status if the limit is exceeded.
- **Error Handling**: The application handles validation errors and rate limit exceedance with appropriate HTTP status codes and error messages.

By following these instructions, you should be able to set up, run, and test the weather application on your local machine successfully.