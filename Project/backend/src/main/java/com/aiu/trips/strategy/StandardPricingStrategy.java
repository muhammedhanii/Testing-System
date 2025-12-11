package com.aiu.trips.strategy;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * StandardPricingStrategy as per Booking_Ticketing.pu diagram
 * Strategy Pattern - Standard pricing without discounts
 */
@Component
public class StandardPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, LocalDateTime bookingDate, int quantity) {
        return basePrice.multiply(BigDecimal.valueOf(quantity));
    }
}
