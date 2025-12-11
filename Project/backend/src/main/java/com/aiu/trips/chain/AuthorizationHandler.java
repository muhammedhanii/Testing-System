package com.aiu.trips.chain;

import com.aiu.trips.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AuthorizationHandler as per Controller.pu diagram
 * Chain of Responsibility Pattern - Validates user authorization/permissions
 */
@Component
public class AuthorizationHandler extends RequestHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void handle(HttpServletRequest request) throws Exception {
        String authHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();

        // Skip for public endpoints
        if (isPublicEndpoint(requestURI)) {
            handleNext(request);
            return;
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String role = jwtUtil.extractRole(token);

            // Check admin-only endpoints
            if (requestURI.contains("/api/admin/") || requestURI.contains("/api/events") && !request.getMethod().equals("GET")) {
                if (!"ADMIN".equals(role) && !"ORGANIZER".equals(role)) {
                    throw new SecurityException("Insufficient permissions for this operation");
                }
            }
        }

        handleNext(request);
    }

    private boolean isPublicEndpoint(String uri) {
        return uri.contains("/api/auth/") || 
               uri.contains("/h2-console") ||
               uri.contains("/swagger");
    }
}
