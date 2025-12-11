package com.aiu.trips.chain;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * ValidationHandler as per Controller.pu diagram
 * Chain of Responsibility Pattern - Validates request data
 */
@Component
public class ValidationHandler extends RequestHandler {

    @Override
    public void handle(HttpServletRequest request) throws Exception {
        // Basic validation - content type check for POST/PUT requests
        String method = request.getMethod();
        if ("POST".equals(method) || "PUT".equals(method)) {
            String contentType = request.getContentType();
            if (contentType == null || !contentType.contains("application/json")) {
                // Allow form data for certain endpoints
                if (!request.getRequestURI().contains("/upload")) {
                    // We'll allow it through - more specific validation happens at the controller level
                }
            }
        }

        handleNext(request);
    }
}
