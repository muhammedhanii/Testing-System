package com.aiu.trips.service;

import com.aiu.trips.model.Booking;
import com.aiu.trips.model.Event;
import com.aiu.trips.repository.BookingRepository;
import com.aiu.trips.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Advanced Analytics Service
 * Provides trend analysis, forecasting, and predictive insights
 */
@Service
public class AdvancedAnalyticsService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Get booking trends over time (last 30 days)
     */
    public Map<String, Object> getBookingTrends(int days) {
        Map<String, Object> trends = new HashMap<>();
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);

        List<Booking> bookings = bookingRepository.findAll().stream()
                .filter(b -> b.getBookingDate() != null && 
                       b.getBookingDate().isAfter(startDate) &&
                       b.getBookingDate().isBefore(endDate))
                .collect(Collectors.toList());

        // Group bookings by date
        Map<String, Long> bookingsByDate = bookings.stream()
                .collect(Collectors.groupingBy(
                    b -> b.getBookingDate().toLocalDate().toString(),
                    Collectors.counting()
                ));

        // Calculate trend direction
        List<Long> counts = new ArrayList<>(bookingsByDate.values());
        String trendDirection = calculateTrendDirection(counts);

        trends.put("period", days + " days");
        trends.put("totalBookings", bookings.size());
        trends.put("bookingsByDate", bookingsByDate);
        trends.put("trendDirection", trendDirection);
        trends.put("averageBookingsPerDay", bookings.size() / (double) days);
        trends.put("peakBookingDay", findPeakDay(bookingsByDate));

        return trends;
    }

    /**
     * Forecast revenue for next period based on historical data
     */
    public Map<String, Object> forecastRevenue(int futureDays) {
        Map<String, Object> forecast = new HashMap<>();
        
        List<Booking> historicalBookings = bookingRepository.findAll();
        
        // Calculate historical average revenue per day
        if (historicalBookings.isEmpty()) {
            forecast.put("forecastedRevenue", 0.0);
            forecast.put("confidence", "LOW");
            forecast.put("message", "Insufficient historical data");
            return forecast;
        }

        double totalRevenue = historicalBookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus()))
                .mapToDouble(Booking::getAmountPaid)
                .sum();

        // Find earliest booking date
        LocalDateTime earliestDate = historicalBookings.stream()
                .map(Booking::getBookingDate)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now().minusDays(30));

        long historicalDays = ChronoUnit.DAYS.between(earliestDate, LocalDateTime.now());
        if (historicalDays == 0) historicalDays = 1;

        double averageRevenuePerDay = totalRevenue / historicalDays;
        double forecastedRevenue = averageRevenuePerDay * futureDays;

        // Calculate trend multiplier (growth or decline)
        double trendMultiplier = calculateRevenueGrowthRate();
        double adjustedForecast = forecastedRevenue * (1 + trendMultiplier);

        forecast.put("forecastPeriod", futureDays + " days");
        forecast.put("baseForecasted Revenue", forecastedRevenue);
        forecast.put("growthRate", trendMultiplier * 100 + "%");
        forecast.put("adjustedForecastedRevenue", adjustedForecast);
        forecast.put("confidence", getConfidenceLevel(historicalBookings.size()));
        forecast.put("historicalAveragePerDay", averageRevenuePerDay);

        return forecast;
    }

    /**
     * Identify popular event categories (based on EventType: EVENT vs TRIP)
     */
    public Map<String, Object> getPopularCategories() {
        Map<String, Object> analysis = new HashMap<>();
        List<Event> events = eventRepository.findAll();

        // Group by type and count
        Map<String, Long> eventsByType = events.stream()
                .filter(e -> e.getType() != null)
                .collect(Collectors.groupingBy(
                    e -> e.getType().toString(),
                    Collectors.counting()
                ));

        // Calculate revenue by type
        Map<String, Double> revenueByType = new HashMap<>();
        for (Event event : events) {
            if (event.getType() != null) {
                String type = event.getType().toString();
                List<Booking> eventBookings = bookingRepository.findByEvent_Id(event.getId());
                double revenue = eventBookings.stream()
                        .filter(b -> "CONFIRMED".equals(b.getStatus()))
                        .mapToDouble(Booking::getAmountPaid)
                        .sum();
                revenueByType.merge(type, revenue, Double::sum);
            }
        }

        String mostPopular = eventsByType.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        String highestRevenue = revenueByType.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        analysis.put("eventsByType", eventsByType);
        analysis.put("revenueByType", revenueByType);
        analysis.put("mostPopularType", mostPopular);
        analysis.put("highestRevenueType", highestRevenue);
        analysis.put("totalTypes", eventsByType.size());

        return analysis;
    }

    /**
     * Analyze attendance patterns
     */
    public Map<String, Object> getAttendancePatterns() {
        Map<String, Object> patterns = new HashMap<>();
        List<Event> events = eventRepository.findAll();

        if (events.isEmpty()) {
            patterns.put("message", "No events available for analysis");
            return patterns;
        }

        // Calculate utilization rates
        List<Double> utilizationRates = new ArrayList<>();
        for (Event event : events) {
            if (event.getCapacity() != null && event.getCapacity() > 0) {
                int booked = event.getCapacity() - (event.getAvailableSeats() != null ? event.getAvailableSeats() : event.getCapacity());
                double rate = (booked * 100.0) / event.getCapacity();
                utilizationRates.add(rate);
            }
        }

        double avgUtilization = utilizationRates.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        double maxUtilization = utilizationRates.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0.0);

        double minUtilization = utilizationRates.stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(0.0);

        patterns.put("averageUtilization", avgUtilization + "%");
        patterns.put("maximumUtilization", maxUtilization + "%");
        patterns.put("minimumUtilization", minUtilization + "%");
        patterns.put("totalEventsAnalyzed", events.size());
        patterns.put("utilizationTrend", avgUtilization > 70 ? "HIGH" : avgUtilization > 40 ? "MEDIUM" : "LOW");

        return patterns;
    }

    /**
     * Identify peak booking periods (day of week, time of day)
     */
    public Map<String, Object> getPeakBookingPeriods() {
        Map<String, Object> peaks = new HashMap<>();
        List<Booking> bookings = bookingRepository.findAll();

        if (bookings.isEmpty()) {
            peaks.put("message", "No bookings available for analysis");
            return peaks;
        }

        // Analyze by day of week
        Map<String, Long> bookingsByDayOfWeek = bookings.stream()
                .filter(b -> b.getBookingDate() != null)
                .collect(Collectors.groupingBy(
                    b -> b.getBookingDate().getDayOfWeek().toString(),
                    Collectors.counting()
                ));

        // Analyze by hour of day
        Map<Integer, Long> bookingsByHour = bookings.stream()
                .filter(b -> b.getBookingDate() != null)
                .collect(Collectors.groupingBy(
                    b -> b.getBookingDate().getHour(),
                    Collectors.counting()
                ));

        String peakDayOfWeek = bookingsByDayOfWeek.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        Integer peakHour = bookingsByHour.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);

        peaks.put("bookingsByDayOfWeek", bookingsByDayOfWeek);
        peaks.put("bookingsByHour", bookingsByHour);
        peaks.put("peakDayOfWeek", peakDayOfWeek);
        peaks.put("peakHour", peakHour + ":00");
        peaks.put("recommendation", "Schedule promotions on " + peakDayOfWeek + " around " + peakHour + ":00");

        return peaks;
    }

    /**
     * Get comprehensive analytics dashboard data
     */
    public Map<String, Object> getComprehensiveAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        analytics.put("bookingTrends", getBookingTrends(30));
        analytics.put("revenueForecast", forecastRevenue(30));
        analytics.put("popularCategories", getPopularCategories());
        analytics.put("attendancePatterns", getAttendancePatterns());
        analytics.put("peakBookingPeriods", getPeakBookingPeriods());
        analytics.put("generatedAt", LocalDateTime.now());

        return analytics;
    }

    // Helper methods

    private String calculateTrendDirection(List<Long> counts) {
        if (counts.size() < 2) return "STABLE";
        
        long sum = 0;
        for (int i = 1; i < counts.size(); i++) {
            sum += (counts.get(i) - counts.get(i - 1));
        }
        
        if (sum > 0) return "INCREASING";
        else if (sum < 0) return "DECREASING";
        else return "STABLE";
    }

    private String findPeakDay(Map<String, Long> bookingsByDate) {
        return bookingsByDate.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }

    private double calculateRevenueGrowthRate() {
        // Simple growth rate calculation based on last 30 days vs previous 30 days
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyDaysAgo = now.minusDays(30);
        LocalDateTime sixtyDaysAgo = now.minusDays(60);

        List<Booking> recent = bookingRepository.findAll().stream()
                .filter(b -> b.getBookingDate() != null &&
                       b.getBookingDate().isAfter(thirtyDaysAgo) &&
                       "CONFIRMED".equals(b.getStatus()))
                .collect(Collectors.toList());

        List<Booking> previous = bookingRepository.findAll().stream()
                .filter(b -> b.getBookingDate() != null &&
                       b.getBookingDate().isAfter(sixtyDaysAgo) &&
                       b.getBookingDate().isBefore(thirtyDaysAgo) &&
                       "CONFIRMED".equals(b.getStatus()))
                .collect(Collectors.toList());

        double recentRevenue = recent.stream().mapToDouble(Booking::getAmountPaid).sum();
        double previousRevenue = previous.stream().mapToDouble(Booking::getAmountPaid).sum();

        if (previousRevenue == 0) return 0;
        return (recentRevenue - previousRevenue) / previousRevenue;
    }

    private String getConfidenceLevel(int dataPoints) {
        if (dataPoints > 100) return "HIGH";
        if (dataPoints > 50) return "MEDIUM";
        if (dataPoints > 20) return "LOW";
        return "VERY LOW";
    }
}
