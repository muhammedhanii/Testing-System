package com.aiu.trips.chain;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RateLimitHandler as per Controller.pu diagram
 * Chain of Responsibility Pattern - Rate limiting for requests
 */
@Component
public class RateLimitHandler extends RequestHandler {

    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private static final long WINDOW_SIZE_MS = 60000; // 1 minute

    private final Map<String, RateLimitInfo> clientRequests = new ConcurrentHashMap<>();

    @Override
    public void handle(HttpServletRequest request) throws Exception {
        String clientId = getClientId(request);
        RateLimitInfo info = clientRequests.computeIfAbsent(clientId, k -> new RateLimitInfo());

        long currentTime = System.currentTimeMillis();
        
        // Reset if window has passed
        if (currentTime - info.windowStart > WINDOW_SIZE_MS) {
            info.reset(currentTime);
        }

        // Check rate limit
        if (info.requestCount >= MAX_REQUESTS_PER_MINUTE) {
            throw new RuntimeException("Rate limit exceeded. Please try again later.");
        }

        info.requestCount++;
        handleNext(request);
    }

    private String getClientId(HttpServletRequest request) {
        // Use IP address as client identifier
        String clientIp = request.getRemoteAddr();
        // In production, you might want to use X-Forwarded-For header for proxied requests
        String forwardedFor = request.getHeader("X-Forwarded-For");
        return forwardedFor != null ? forwardedFor : clientIp;
    }

    private static class RateLimitInfo {
        long windowStart;
        int requestCount;

        RateLimitInfo() {
            reset(System.currentTimeMillis());
        }

        void reset(long time) {
            this.windowStart = time;
            this.requestCount = 0;
        }
    }
}
