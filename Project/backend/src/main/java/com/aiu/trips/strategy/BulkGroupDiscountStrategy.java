package com.aiu.trips.strategy;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * BulkGroupDiscountStrategy as per Booking_Ticketing.pu diagram
 * Strategy Pattern - Bulk/group discount pricing
 */
@Component
public class BulkGroupDiscountStrategy implements PricingStrategy {

    private static final int THRESHOLD = 5; // Minimum 5 tickets for group discount
    private static final BigDecimal DISCOUNT_PERCENT = new BigDecimal("0.20"); // 20% discount

    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, LocalDateTime bookingDate, int quantity) {
        BigDecimal totalPrice = basePrice.multiply(BigDecimal.valueOf(quantity));
        
        // Apply group discount if quantity meets threshold
        if (quantity >= THRESHOLD) {
            BigDecimal discount = totalPrice.multiply(DISCOUNT_PERCENT);
            return totalPrice.subtract(discount);
        }
        
        return totalPrice;
    }
}
