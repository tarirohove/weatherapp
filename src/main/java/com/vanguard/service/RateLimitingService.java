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

    private Map<String, Bucket> bucketMap;

    public RateLimitingService(@Value("${openweathermap.api.key.list}") String[] validApiKeyTokens) {
        Map<String,Bucket> abucketMap = new HashMap<>();
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofHours(1)));
        Arrays.stream(validApiKeyTokens).forEach(token -> {
            Bucket bucket = Bucket.builder()
                    .addLimit(limit)
                    .build();
            abucketMap.put(token,bucket);

        });
        this.bucketMap = abucketMap;
    }

    public Bucket resolveBucket(String apiKey) {
        return this.bucketMap.get(apiKey);
    }

    public boolean allowRequest(String apiKey) {
        Bucket bucket = bucketMap.get(apiKey);
        if (bucket == null) {
            throw new IllegalArgumentException("Invalid API key");
        }
        return bucket.tryConsume(1);
    }
}