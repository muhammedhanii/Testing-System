package com.aiu.trips.config;

import com.aiu.trips.chain.RequestHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Test Configuration - Provides a no-op request handler chain for testing
 */
@TestConfiguration
public class TestConfig {

    @Bean("requestHandlerChain")
    @Primary
    public RequestHandler requestHandlerChain() {
        return new RequestHandler() {
            @Override
            public void handle(HttpServletRequest request) throws Exception {
                // No-op for tests - skip all validation
                if (next != null) {
                    next.handle(request);
                }
            }
        };
    }
}
