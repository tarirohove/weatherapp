package com.vanguard.service;

import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitingServiceTest {

    private RateLimitingService rateLimitingService;
    @Mock
    private Bucket bucket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String[] apiKeys = {"validApiKey1", "validApiKey2", "validApiKey3"};
        rateLimitingService = new RateLimitingService(apiKeys);
    }

    @Test
    void testConstructorWithValidApiKeys() {
        String[] apiKeys = {"validApiKey1", "validApiKey2"};
        RateLimitingService service = new RateLimitingService(apiKeys);
        assertNotNull(service);
        assertEquals(2, service.bucketMap.size());
    }

    @Test
    void testConstructorWithEmptyApiKeys() {
        String[] apiKeys = {};
        RateLimitingService service = new RateLimitingService(apiKeys);
        assertNotNull(service);
        assertEquals(0, service.bucketMap.size());
    }

    @Test
    void testAllowRequestWithValidApiKey() {
        String apiKeyFromUser = "validApiKey1";
        boolean result = rateLimitingService.allowRequest(apiKeyFromUser);
        assertTrue(result);
    }

    @Test
    void testAllowRequestWithInvalidApiKey() {
        String invalidApiKeyFromUser = "invalid_key";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rateLimitingService.allowRequest(invalidApiKeyFromUser);
        });
        assertEquals("Invalid API key", exception.getMessage());
    }

    @Test
    void testAllowRequestExceedingRateLimit() {
        String apiKeyFromUser = "validApiKey1";
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimitingService.allowRequest(apiKeyFromUser));
        }
        assertFalse(rateLimitingService.allowRequest(apiKeyFromUser));
    }
}
