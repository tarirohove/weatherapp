package com.vanguard.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class RateLimitingService {
    Map<String, Bucket> bucketMap;
    private final static int BUCKET_CAPACITY = 5;
    private final static int TOKENS_NUMBER = 5;

    public RateLimitingService(@Value("${openweathermap.api.key.list}") String[] validApiKeyTokens) {
        Map<String, Bucket> abucketMap = new HashMap<>();
        Bandwidth limit = Bandwidth.classic(BUCKET_CAPACITY, Refill.greedy(TOKENS_NUMBER, Duration.ofHours(1)));
        Arrays.stream(validApiKeyTokens).forEach(token -> {
            Bucket bucket = Bucket.builder()
                    .addLimit(limit)
                    .build();
            abucketMap.put(token, bucket);

        });
        this.bucketMap = abucketMap;
    }

    public boolean allowRequest(String apiKey) {
        Bucket bucket = bucketMap.get(apiKey);
        if (bucket == null) {
            throw new IllegalArgumentException("Invalid API key");
        }
        return bucket.tryConsume(1);
    }
}