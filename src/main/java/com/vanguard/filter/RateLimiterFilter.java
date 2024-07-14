package com.vanguard.filter;

import com.vanguard.service.RateLimitingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RateLimiterFilter extends OncePerRequestFilter {

    private final RateLimitingService rateLimitingService;

    public RateLimiterFilter(RateLimitingService rateLimitingService) {
        this.rateLimitingService = rateLimitingService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader("X-API-KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing API key");
            return;
        }

        if (!rateLimitingService.allowRequest(apiKey)) {
            response.sendError( HttpStatus.TOO_MANY_REQUESTS.value(), "Hourly rate limit has been exceeded for the provided API key.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

