package com.aiu.trips.strategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * PricingStrategy interface as per Booking_Ticketing.pu diagram
 * Strategy Pattern - Interface for pricing strategies
 */
public interface PricingStrategy {
    BigDecimal calculatePrice(BigDecimal basePrice, LocalDateTime bookingDate, int quantity);
}
