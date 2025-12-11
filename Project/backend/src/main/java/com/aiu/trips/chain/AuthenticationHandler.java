package com.aiu.trips.chain;

import com.aiu.trips.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AuthenticationHandler as per Controller.pu diagram
 * Chain of Responsibility Pattern - Validates authentication token
 */
@Component
public class AuthenticationHandler extends RequestHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void handle(HttpServletRequest request) throws Exception {
        String authHeader = request.getHeader("Authorization");
        
        // Skip authentication for public endpoints
        String requestURI = request.getRequestURI();
        if (isPublicEndpoint(requestURI)) {
            handleNext(request);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("Missing or invalid authentication token");
        }

        String token = authHeader.substring(7);
        try {
            String email = jwtUtil.extractEmail(token);
            if (!jwtUtil.validateToken(token, email)) {
                throw new SecurityException("Invalid or expired token");
            }
        } catch (Exception e) {
            throw new SecurityException("Invalid or expired token");
        }

        handleNext(request);
    }

    private boolean isPublicEndpoint(String uri) {
        return uri.contains("/api/auth/login") || 
               uri.contains("/api/auth/register") ||
               uri.contains("/h2-console") ||
               uri.contains("/swagger") ||
               uri.contains("/actuator");
    }
}
