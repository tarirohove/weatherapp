package com.vanguard.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RateLimiterFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${openweathermap.api.key.list}") String[] apiKeys;

    @Test
    public void whenRateLimitExceeded_thenTooManyRequests() throws Exception {

        for (int i = 0; i < 6; i++) {
            mockMvc.perform(get("/weather/latest")
                            .header("X-API-KEY", apiKeys[0])
                            .param("city", "London")
                            .param("country", "UK"))
                    .andExpect(i < 5 ? status().isOk() : status().isTooManyRequests());
        }
    }
}
