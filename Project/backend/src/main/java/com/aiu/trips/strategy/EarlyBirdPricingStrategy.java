package com.aiu.trips.strategy;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * EarlyBirdPricingStrategy as per Booking_Ticketing.pu diagram
 * Strategy Pattern - Early bird discount pricing
 */
@Component
public class EarlyBirdPricingStrategy implements PricingStrategy {

    private static final int EARLY_BIRD_DAYS = 7; // Book 7 days in advance
    private static final BigDecimal DISCOUNT_PERCENT = new BigDecimal("0.15"); // 15% discount

    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, LocalDateTime bookingDate, int quantity) {
        // Calculate days until event (assuming event date would be passed separately)
        // For now, we'll apply discount if booking is made early (simplified)
        BigDecimal totalPrice = basePrice.multiply(BigDecimal.valueOf(quantity));
        
        // Apply early bird discount
        BigDecimal discount = totalPrice.multiply(DISCOUNT_PERCENT);
        return totalPrice.subtract(discount);
    }
}
