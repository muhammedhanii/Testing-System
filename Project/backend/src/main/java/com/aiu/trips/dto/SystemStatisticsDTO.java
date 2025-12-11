package com.aiu.trips.dto;

/**
 * SystemStatisticsDTO for transferring system statistics
 */
public class SystemStatisticsDTO {
    private Long totalUsers;
    private Integer totalEvents;
    private Long totalActivities;
    private Long totalBookings;
    private Long activeEvents;
    private Double totalRevenue;
    private Double averageRating;
    private Double averageAttendance;

    public SystemStatisticsDTO() {}

    // Getters and Setters
    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }

    public Integer getTotalEvents() { return totalEvents; }
    public void setTotalEvents(Integer totalEvents) { this.totalEvents = totalEvents; }

    public Long getTotalActivities() { return totalActivities; }
    public void setTotalActivities(Long totalActivities) { this.totalActivities = totalActivities; }

    public Long getTotalBookings() { return totalBookings; }
    public void setTotalBookings(Long totalBookings) { this.totalBookings = totalBookings; }

    public Long getActiveEvents() { return activeEvents; }
    public void setActiveEvents(Long activeEvents) { this.activeEvents = activeEvents; }

    public Double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Double getAverageAttendance() { return averageAttendance; }
    public void setAverageAttendance(Double averageAttendance) { this.averageAttendance = averageAttendance; }
}
