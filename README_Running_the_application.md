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