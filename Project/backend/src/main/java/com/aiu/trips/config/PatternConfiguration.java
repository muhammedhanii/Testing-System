package com.aiu.trips.config;

import com.aiu.trips.chain.*;
import com.aiu.trips.decorator.*;
import com.aiu.trips.strategy.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for design patterns
 */
@Configuration
public class PatternConfiguration {

    /**
     * Configure Chain of Responsibility
     */
    @Bean
    public RequestHandler requestHandlerChain(
            AuthenticationHandler authHandler,
            AuthorizationHandler authzHandler,
            ValidationHandler validationHandler,
            RateLimitHandler rateLimitHandler) {
        
        // Build the chain
        authHandler.setNext(authzHandler);
        authzHandler.setNext(validationHandler);
        validationHandler.setNext(rateLimitHandler);
        
        return authHandler;
    }

    /**
     * Configure Ticket Service with Decorators
     */
    @Bean
    public ITicketService ticketService(BaseTicketService baseService) {
        // Wrap with decorators
        ITicketService signed = new SignedQrDecorator(baseService);
        ITicketService audited = new AuditLogDecorator(signed);
        return audited;
    }

    /**
     * Configure default Pricing Strategy
     */
    @Bean
    public PricingStrategy defaultPricingStrategy() {
        return new StandardPricingStrategy();
    }
}
